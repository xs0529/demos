package com.example.demo.jwtsecurity.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.jwtsecurity.common.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.jwtsecurity.system.service.RoleResourceService;
import com.example.demo.jwtsecurity.system.entity.RoleResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *  @description : RoleResource 控制器
 *  ---------------------------------
 *   @author 谢霜
 *  @since 2019-01-07
 */
@RestController
@RequestMapping("/roleResource")
public class RoleResourceController {
    private final Logger logger = LoggerFactory.getLogger(RoleResourceController.class);
    @Autowired
    private RoleResourceService roleResourceService;

    /**
     * @description : 获取分页列表
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/list")
    public Object getRoleResourceList(RoleResource param ,@RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "size",defaultValue = "10") Integer size) {
        Page<RoleResource> roleResourcepage = new Page<>(page,size);
        return roleResourceService.page(roleResourcepage,null);
    }

    /**
     * @description : 通过id获取RoleResource
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @GetMapping("/get/{id}")
    public Result getRoleResourceById(@PathVariable(value = "id") String id) {
        try {
            RoleResource param= roleResourceService.getById(id);
            return Result.ok(param);
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("获取信息失败");
        }
    }

    /**
     * @description : 通过id删除RoleResource
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/delete/{id}")
    public Result deleteRoleResourceById(@PathVariable(value = "id") String id) {
        List<Integer> list = Arrays.asList(id.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        try{
            if (roleResourceService.removeByIds(list)){
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
     * @description : 通过id更新RoleResource
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/update")
    public Object updateRoleResourceById(RoleResource param) {
        try{
            if (roleResourceService.updateById(param)){
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
     * @description : 添加RoleResource
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/add")
    public Object addRoleResource(RoleResource param) {
        try{
            if (roleResourceService.save(param)){
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
