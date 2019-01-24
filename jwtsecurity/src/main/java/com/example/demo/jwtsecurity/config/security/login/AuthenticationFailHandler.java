package com.example.demo.jwtsecurity.config.security.login;

import com.example.demo.jwtsecurity.common.util.ResponseUtils;
import com.example.demo.jwtsecurity.config.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 谢霜
 * @Date: 2018/09/13 上午 10:25
 * @Description: 认证失败功的处理类
 */
@Slf4j
@Component
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${loginTimeLimit}")
    private Integer loginTimeLimit;

    @Value("${loginAfterTime}")
    private Integer loginAfterTime;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            String username = request.getParameter("username");
            recordLoginTime(username);
            String key = "loginTimeLimit:"+username;
            String value = (String) redisUtils.get(key);
            if(StringUtils.isBlank(value)){
                value = "0";
            }
            //获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = loginTimeLimit - loginFailTime;
            log.info("用户"+username+"登录失败，还有"+restLoginTime+"次机会");
            if(restLoginTime<=3&&restLoginTime>0){
                ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"用户名或密码错误，还有"+restLoginTime+"次尝试机会"));
            } else if(restLoginTime<=0) {
                ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"登录错误次数超过限制，请"+loginAfterTime+"分钟后再试"));
            } else {
                ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
            ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"账户被禁用，请联系管理员"));
        } else {
            ResponseUtils.out(response, ResponseUtils.resultMap(false,401,"登录失败，其他内部错误"));
        }
    }

    /**
     * 判断用户登陆错误次数
     */
    public boolean recordLoginTime(String username){

        String key = "loginTimeLimit:"+username;
        String flagKey = "loginFailFlag:"+username;
        String value = (String) redisUtils.get(key);
        if(StringUtils.isBlank(value)){
            value = "0";
        }
        //获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redisUtils.set(key, String.valueOf(loginFailTime),loginAfterTime*60);
        if(loginFailTime>=loginTimeLimit){

            redisUtils.set(flagKey, "fail", loginAfterTime*60);
            return false;
        }
        return true;
    }
}