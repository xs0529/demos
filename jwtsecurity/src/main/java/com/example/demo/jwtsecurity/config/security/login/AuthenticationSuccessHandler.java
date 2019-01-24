package com.example.demo.jwtsecurity.config.security.login;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.jwtsecurity.common.util.ResponseUtils;
import com.example.demo.jwtsecurity.config.redis.RedisUtils;
import com.example.demo.jwtsecurity.config.security.jwt.MySecurityConstant;
import com.example.demo.jwtsecurity.config.security.jwt.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author: 谢霜
 * @Date: 2018/09/13 上午 10:26
 * @Description: 认证成功的处理类
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${tokenExpireTime}")
    private Integer tokenExpireTime;
    @Value("${saveLoginTime}")
    private Integer saveLoginTime;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //用户选择保存登录状态几天
        String saveTime = request.getParameter(MySecurityConstant.SAVE_LOGIN);
        if(StringUtils.isNotBlank(saveTime)&&Boolean.valueOf(saveTime)){
            tokenExpireTime = saveLoginTime * 24;
        }
        String username = ((UserDetails)authentication.getPrincipal()).getUsername();
        Collection<SimpleGrantedAuthority> collection = (Collection<SimpleGrantedAuthority>) ((UserDetails) authentication.getPrincipal()).getAuthorities();
        List<String> authorities = new ArrayList<>();
        for(SimpleGrantedAuthority g : collection){
            authorities.add(g.getAuthority());
        }
        //登陆成功生成JWT
        String token = Jwts.builder()
                //主题 放入用户名
                .setSubject(username)
                //自定义属性 放入用户拥有请求权限
                .claim(MySecurityConstant.AUTHORITIES, JSONObject.toJSONString(authorities))
                //失效时间
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 3600 * 1000))
                //签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, MySecurityConstant.JWT_SIGN_KEY)
                .compact();
        // 再次加密，解密时分割此段
        token = MySecurityConstant.TOKEN_SPLIT + token;
        response.setHeader("accessToken",token);
        tokenService.toBlacklist(username);
        redisUtils.set(MySecurityConstant.USER_TOKEN+":"+username,token,tokenExpireTime*3600);
        ResponseUtils.out(response, ResponseUtils.resultMap(true,200,"登录成功",token));
    }
}