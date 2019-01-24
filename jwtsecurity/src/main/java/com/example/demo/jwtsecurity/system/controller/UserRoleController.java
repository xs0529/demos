package com.example.demo.jwtsecurity.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.jwtsecurity.common.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.jwtsecurity.system.service.UserRoleService;
import com.example.demo.jwtsecurity.system.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *  @description : UserRole 控制器
 *  ---------------------------------
 *   @author 谢霜
 *  @since 2019-01-07
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController {
    private final Logger logger = LoggerFactory.getLogger(UserRoleController.class);
    @Autowired
    private UserRoleService userRoleService;

    /**
     * @description : 获取分页列表
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/list")
    public Object getUserRoleList(UserRole param ,@RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "size",defaultValue = "10") Integer size) {
        Page<UserRole> userRolepage = new Page<>(page,size);
        return userRoleService.page(userRolepage,null);
    }

    /**
     * @description : 通过id获取UserRole
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @GetMapping("/get/{id}")
    public Result getUserRoleById(@PathVariable(value = "id") String id) {
        try {
            UserRole param= userRoleService.getById(id);
            return Result.ok(param);
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("获取信息失败");
        }
    }

    /**
     * @description : 通过id删除UserRole
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/delete/{id}")
    public Result deleteUserRoleById(@PathVariable(value = "id") String id) {
        List<Integer> list = Arrays.asList(id.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        try{
            if (userRoleService.removeByIds(list)){
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
     * @description : 通过id更新UserRole
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/update")
    public Object updateUserRoleById(UserRole param) {
        try{
            if (userRoleService.updateById(param)){
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
     * @description : 添加UserRole
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/add")
    public Object addUserRole(UserRole param) {
        try{
            if (userRoleService.save(param)){
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
