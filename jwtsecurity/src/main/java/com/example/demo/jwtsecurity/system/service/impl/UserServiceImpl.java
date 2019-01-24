package com.example.demo.jwtsecurity.system.service.impl;

import com.example.demo.jwtsecurity.system.entity.User;
import com.example.demo.jwtsecurity.system.entity.UserRole;
import com.example.demo.jwtsecurity.system.mapper.UserMapper;
import com.example.demo.jwtsecurity.system.service.UserRoleService;
import com.example.demo.jwtsecurity.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user, Integer[] roleIds) throws Exception {
        if (save(user)){
            if (roleIds!=null&&roleIds.length>0){
                List<UserRole> userRoles = new ArrayList<>();
                for (int i = 0; i < roleIds.length; i++) {
                    UserRole userRole = new UserRole();
                    userRole.setUid(user.getId());
                    userRole.setRid(roleIds[i]);
                    userRoles.add(userRole);
                }
                if (userRoleService.saveBatch(userRoles)){
                    return true;
                }else {
                    throw new Exception();
                }
            }
            return true;
        }else {
            return false;
        }
    }
}
