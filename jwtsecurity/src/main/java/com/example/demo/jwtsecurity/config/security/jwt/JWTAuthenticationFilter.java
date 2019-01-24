package com.example.demo.jwtsecurity.config.security.jwt;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.jwtsecurity.common.util.ResponseUtils;
import com.example.demo.jwtsecurity.config.redis.RedisUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 谢霜
 * @Date: 2018/09/13 上午 10:26
 * @Description: JWT过滤器
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private RedisUtils redisUtils;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, RedisUtils redisUtils) {
        super(authenticationManager);
        this.redisUtils = redisUtils;
    }

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(MySecurityConstant.HEADER);
        if(StringUtils.isBlank(header)){
            header = request.getParameter(MySecurityConstant.HEADER);
        }
        if (StringUtils.isBlank(header) || !header.startsWith(MySecurityConstant.TOKEN_SPLIT)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }catch (Exception e){

        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader(MySecurityConstant.HEADER);
        if (StringUtils.isNotBlank(token)) {
            // 解析token
            Claims claims = null;
            try {
                claims = Jwts.parser()
                        .setSigningKey(MySecurityConstant.JWT_SIGN_KEY)
                        .parseClaimsJws(token.replace(MySecurityConstant.TOKEN_SPLIT, ""))
                        .getBody();
                if (redisUtils.hasKey(MySecurityConstant.TOKEN_BLACKLIST+":"+token)){
                    throw new MissingClaimException(new DefaultHeader(),claims,"token已失效");
                }
                //获取用户名
                String username = claims.getSubject();
                //获取权限
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                String authority = claims.get(MySecurityConstant.AUTHORITIES).toString();

                if(StringUtils.isNotBlank(authority)){
                    List<String> list = JSONObject.parseObject(authority,ArrayList.class);
                    for(String ga : list){
                        authorities.add(new SimpleGrantedAuthority(ga));
                    }
                }
                if(StringUtils.isNotBlank(username)) {
                    User principal = new User(username, "", authorities);
                    return new UsernamePasswordAuthenticationToken(principal, null, authorities);
                }
            } catch (InvalidClaimException e){
                ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"token已失效"));
            } catch (ExpiredJwtException e) {
                ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"token已过期"));
            } catch (Exception e){
                ResponseUtils.out(response, ResponseUtils.resultMap(false,400,"解析token错误"));
            }
        }
        return null;
    }
}
