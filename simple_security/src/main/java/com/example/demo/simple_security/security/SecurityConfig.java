package com.example.demo.simple_security.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

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
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new MyUserDetailsService());
    }

    @Data
    class Permission{
        private String url;
        private String ROLE;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //构造权限
        Set<Permission> set = new HashSet<>();
        Permission permission  = new Permission();
        permission.setUrl("/admin/**");
        permission.setROLE("ADMIN");
        set.add(permission);
        Permission permission1 = new Permission();
        permission1.setUrl("/role");
        permission1.setROLE("USER");
        set.add(permission1);

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http.formLogin().permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/test/**").permitAll();
        set.forEach(permission2 -> {
                    urlRegistry.antMatchers(permission2.getUrl()).hasRole(permission2.getROLE());
            });
            urlRegistry.anyRequest().authenticated();

    }


    class MyUserDetailsService implements UserDetailsService {

        private Map<String, User> userRepository = new HashMap<String, User>();

        public MyUserDetailsService() {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            User user1 = new User("user1", "password1", authorities);
            userRepository.put("user1", user1);
            User user2 = new User("user2", "password2", authorities);
            userRepository.put("user2", user2);
        }

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            User user = userRepository.get(s);
            return user;
        }
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
