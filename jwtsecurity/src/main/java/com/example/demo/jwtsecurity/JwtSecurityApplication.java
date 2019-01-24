package com.example.demo.jwtsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@SpringBootApplication
@RestController
public class JwtSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtSecurityApplication.class, args);
    }

    @GetMapping("test")
    public String test(){
        return "test!";
    }

    @GetMapping("role")
    public String role(){
        return "role!";
    }

    @GetMapping("admin")
    public String admin(){
        return "admin!";
    }
}

