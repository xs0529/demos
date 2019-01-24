package com.example.demo.jwtsecurity.system.service;

import com.example.demo.jwtsecurity.system.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 根据用户id获取用户的权限
     * @param userId
     * @return
     */
    Set<Resource> getResourceByUserId(Integer userId);
}
