package com.example.demo.jwtsecurity.system.service.impl;

import com.example.demo.jwtsecurity.config.security.Url;
import com.example.demo.jwtsecurity.system.entity.Resource;
import com.example.demo.jwtsecurity.system.entity.Role;
import com.example.demo.jwtsecurity.system.mapper.ResourceMapper;
import com.example.demo.jwtsecurity.system.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Set<Resource> getResourceByUserId(Integer userId) {
        return resourceMapper.getResourceByUserId(userId);
    }

    @Override
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> loaderUrlAndRole() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        Set<Url> allUrlAndRole = resourceMapper.getAllUrlAndRole();
        if (!CollectionUtils.isEmpty(allUrlAndRole)){
            allUrlAndRole.forEach(url -> {
                if (!StringUtils.isEmpty(url.getUrl()) && !CollectionUtils.isEmpty(url.getRoles())){
                    RequestMatcher requestMatcher = new AntPathRequestMatcher(url.getUrl());
                    List<ConfigAttribute> configAttributes = new ArrayList<>();
                    url.getRoles().forEach(role -> {
                        SecurityConfig securityConfig = new SecurityConfig("ROLE_"+role.getName());
                        configAttributes.add(securityConfig);
                    });
                    requestMap.put(requestMatcher, configAttributes);
                }
            });
            return requestMap;
        }else {
            return null;
        }
    }

    private static String hasAnyRole(String... authorities) {
        String anyAuthorities = StringUtils.arrayToDelimitedString(authorities, "','ROLE_");
        return "hasAnyRole('ROLE_" + anyAuthorities + "')";
    }
}
