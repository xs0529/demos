package com.example.demo.jwtsecurity.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.jwtsecurity.common.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.jwtsecurity.system.service.RoleService;
import com.example.demo.jwtsecurity.system.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *  @description : Role 控制器
 *  ---------------------------------
 *   @author 谢霜
 *  @since 2019-01-07
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;

    /**
     * @description : 获取分页列表
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/list")
    public Result getRoleList(Role param ,@RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "size",defaultValue = "10") Integer size) {
        Page<Role> rolepage = new Page<>(page,size);
        IPage<Role> iPage = roleService.page(rolepage, null);
        return Result.ok(iPage);
    }

    /**
     * @description : 通过id获取Role
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @GetMapping("/get/{id}")
    public Result getRoleById(@PathVariable(value = "id") String id) {
        try {
            Role param= roleService.getById(id);
            return Result.ok(param);
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("获取信息失败");
        }
    }

    /**
     * @description : 通过id删除Role
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/delete/{id}")
    public Result deleteRoleById(@PathVariable(value = "id") String id) {
        List<Integer> list = Arrays.asList(id.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        try{
            if (roleService.removeByIds(list)){
                return Result.ok();
            } else {
                return Result.fail();
            }
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("删除信息失败");
        }
    }

    /**
     * @description : 通过id更新Role
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/update")
    public Object updateRoleById(Role param) {
        try{
            if (roleService.updateById(param)){
                return Result.ok();
            } else {
                return Result.fail();
            }
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("修改信息失败");
        }
    }

    /**
     * @description : 添加Role
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/add")
    public Object addRole(Role param) {
        try{
            if (roleService.save(param)){
                return Result.ok();
            } else {
            return Result.fail();
            }
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("添加信息失败");
        }
    }
}
