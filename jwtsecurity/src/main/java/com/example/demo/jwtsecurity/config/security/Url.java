package com.example.demo.jwtsecurity.config.security;

import com.example.demo.jwtsecurity.system.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2019-01-25
 */
@Data
public class Url {
    private Integer id;
    private String url;
    private List<Role> roles;
}
