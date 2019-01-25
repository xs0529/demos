package com.example.demo.jwtsecurity.system.service.impl;

import com.example.demo.jwtsecurity.SimpleSecurityApplicationTests;
import com.example.demo.jwtsecurity.config.security.Url;
import com.example.demo.jwtsecurity.system.mapper.ResourceMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * <p>
 *
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2019-01-25
 */
public class ResourceServiceImplTest extends SimpleSecurityApplicationTests {

    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    public void test(){
        Set<Url> allUrlAndRole = resourceMapper.getAllUrlAndRole();
        System.out.println(allUrlAndRole);
    }

}