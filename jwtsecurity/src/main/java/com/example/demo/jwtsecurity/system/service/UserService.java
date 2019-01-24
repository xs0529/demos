package com.example.demo.jwtsecurity.system.service;

import com.example.demo.jwtsecurity.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
public interface UserService extends IService<User> {
    /**
     * 添加用户
     * @param user
     * @param roleIds 权限id数组
     * @return
     * @throws Exception
     */
    boolean save(User user, Integer[] roleIds) throws Exception;
}
