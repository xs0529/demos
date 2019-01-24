package com.example.demo.jwtsecurity.config.security;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.jwtsecurity.config.redis.RedisUtils;
import com.example.demo.jwtsecurity.config.security.jwt.JWTAuthenticationFilter;
import com.example.demo.jwtsecurity.config.security.login.AuthenticationFailHandler;
import com.example.demo.jwtsecurity.config.security.login.AuthenticationSuccessHandler;
import com.example.demo.jwtsecurity.system.entity.Resource;
import com.example.demo.jwtsecurity.system.entity.User;
import com.example.demo.jwtsecurity.system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2019-01-06
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailHandler failHandler;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new MyUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http
                .authorizeRequests();
        // 所有资源
        List<Resource> resourceList = resourceService.list(null);
        if (!CollectionUtils.isEmpty(resourceList)){
            resourceList.forEach(permission -> {
                if (StrUtil.isNotBlank(permission.getUrl())){
                    if (permission.getVerification().equals(1)){
                        urlRegistry.antMatchers(permission.getUrl()).hasRole(permission.getPermission());
                    }else {
                        urlRegistry.antMatchers(permission.getUrl()).permitAll();
                    }
                }
            });
        }
        urlRegistry
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failHandler)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),redisUtils));
        http.headers().cacheControl();
    }


    class MyUserDetailsService implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            com.example.demo.jwtsecurity.system.entity.User user = new com.example.demo.jwtsecurity.system.entity.User();
            user.setUsername(s);
            user = user.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername,user.getUsername()));
            if (user==null){
                throw new UsernameNotFoundException("用户名错误");
            }else {
                Set<Resource> resourceSet = resourceService.getResourceByUserId(user.getId());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (!CollectionUtils.isEmpty(resourceSet)){
                    resourceSet.forEach(resource -> {
                        authorities.add(new SimpleGrantedAuthority("ROLE_"+resource.getPermission()));
                    });
                }
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),user.getIsUsable()==1?true:false,true,true,true,authorities);
            }
        }
    }

}
