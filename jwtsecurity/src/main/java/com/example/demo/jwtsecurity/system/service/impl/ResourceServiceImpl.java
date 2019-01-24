package com.example.demo.jwtsecurity.system.service.impl;

import com.example.demo.jwtsecurity.system.entity.Resource;
import com.example.demo.jwtsecurity.system.mapper.ResourceMapper;
import com.example.demo.jwtsecurity.system.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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
}
