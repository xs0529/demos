package com.example.demo.jwtsecurity.system.service.impl;

import com.example.demo.jwtsecurity.system.entity.Role;
import com.example.demo.jwtsecurity.system.mapper.RoleMapper;
import com.example.demo.jwtsecurity.system.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleByUserId(Integer userId) {
        return roleMapper.getRoleByUserId(userId);
    }
}
