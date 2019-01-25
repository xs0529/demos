package com.example.demo.jwtsecurity.system.mapper;

import com.example.demo.jwtsecurity.system.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoleByUserId(Integer userId);
}
