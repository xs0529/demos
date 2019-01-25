package com.example.demo.jwtsecurity.config.security;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.jwtsecurity.config.redis.RedisUtils;
import com.example.demo.jwtsecurity.config.security.jwt.JWTAuthenticationFilter;
import com.example.demo.jwtsecurity.config.security.login.AuthenticationFailHandler;
import com.example.demo.jwtsecurity.config.security.login.AuthenticationSuccessHandler;
import com.example.demo.jwtsecurity.system.entity.Resource;
import com.example.demo.jwtsecurity.system.entity.Role;
import com.example.demo.jwtsecurity.system.entity.User;
import com.example.demo.jwtsecurity.system.service.ResourceService;
import com.example.demo.jwtsecurity.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
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
    private RoleService roleService;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailHandler failHandler;
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new MyUserDetailsServiceImpl()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http
                .authorizeRequests();
        urlRegistry
                .and()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failHandler)
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 最后添加此filter才能保证所有的请求都需要认证
                .addFilterAfter(new MyFilterSecurityInterceptor(),MyFilterSecurityInterceptor.class)
                .addFilter(new JWTAuthenticationFilter(authenticationManager(),redisUtils));
        http.headers().cacheControl();

        MyFilterInvocationSecurityMetadataSource.loaderUrlAndRole(resourceService.loaderUrlAndRole());
    }

    /**
     * 加载用户信息
     */
    private class MyUserDetailsServiceImpl implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            com.example.demo.jwtsecurity.system.entity.User user = new com.example.demo.jwtsecurity.system.entity.User();
            user.setUsername(s);
            user = user.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername,user.getUsername()));
            if (user==null){
                throw new UsernameNotFoundException("用户名错误");
            }else {
                List<Role> roles = roleService.getRoleByUserId(user.getId());
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (!CollectionUtils.isEmpty(roles)){
                    roles.forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
                    });
                }
                return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),user.getIsUsable()==1?true:false,true,true,true,authorities);
            }
        }
    }

    /**
     * 自定义加载url必须要的filter
     */
    private class MyFilterSecurityInterceptor extends FilterSecurityInterceptor {
        public MyFilterSecurityInterceptor() throws Exception {
            this.setSecurityMetadataSource(new MyFilterInvocationSecurityMetadataSource());
            this.setAccessDecisionManager(new MyAccessDecisionManager());
            this.setAuthenticationManager(authenticationManagerBean());
        }
    }

    /**
     * 自定义加载url需要的权限判断器
     */
    private class MyAccessDecisionManager implements AccessDecisionManager {

        @Override
        public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
            if (configAttributes == null) {
                return;
            }
            Iterator<ConfigAttribute> ite = configAttributes.iterator();
            while (ite.hasNext()) {
                ConfigAttribute ca = ite.next();
                String needRole = ca.getAttribute();
                // ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
                for (GrantedAuthority ga : authentication.getAuthorities()) {
                    if (needRole.trim().equals(ga.getAuthority().trim())) {
                        return;
                    }
                }
            }
            throw new AccessDeniedException("抱歉，您没有访问权限");
        }

        @Override
        public boolean supports(ConfigAttribute configAttribute) {
            return true;
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return true;
        }
    }

}
