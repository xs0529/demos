package com.example.demo.jwtsecurity.system.mapper;

import com.example.demo.jwtsecurity.config.security.Url;
import com.example.demo.jwtsecurity.system.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据用户id获取用户的权限
     * @param userId
     * @return
     */
    Set<Resource> getResourceByUserId(Integer userId);

    Set<Url> getAllUrlAndRole();
}
