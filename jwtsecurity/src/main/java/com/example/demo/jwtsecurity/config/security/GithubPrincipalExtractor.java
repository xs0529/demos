package com.example.demo.jwtsecurity.config.security;


import com.example.demo.jwtsecurity.system.entity.Role;
import com.example.demo.jwtsecurity.system.entity.User;
import com.example.demo.jwtsecurity.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GithubPrincipalExtractor extends AbstractPrincipalExtractor {
    @Autowired
    UserService userService;
    @Override
    public User getUserByOpenId(String id) {
        System.out.println("GithubPrincipalExtractor");
        return userService.getById(id);
    }

    @Override
    public Role getUserRoleByOauth2ClientName() {
        Role role = new Role();
        role.setName("GITHUB");
        return role;
    }
}
