package com.example.demo.jwtsecurity.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.jwtsecurity.common.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.jwtsecurity.system.service.UserService;
import com.example.demo.jwtsecurity.system.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *  @description : User 控制器
 *  ---------------------------------
 *   @author 谢霜
 *  @since 2019-01-07
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * @description : 获取分页列表
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/list")
    public Object getUserList(User param ,@RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "size",defaultValue = "10") Integer size) {
        Page<User> userpage = new Page<>(page,size);
        return userService.page(userpage,null);
    }

    /**
     * @description : 通过id获取User
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @GetMapping("/get/{id}")
    public Result getUserById(@PathVariable(value = "id") String id) {
        try {
            User param= userService.getById(id);
            return Result.ok(param);
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("获取信息失败");
        }
    }

    /**
     * @description : 通过id删除User
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/delete/{id}")
    public Result deleteUserById(@PathVariable(value = "id") String id) {
        List<Integer> list = Arrays.asList(id.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        try{
            if (userService.removeByIds(list)){
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
     * @description : 通过id更新User
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/update")
    public Object updateUserById(User param) {
        try{
            if (userService.updateById(param)){
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
     * @description : 添加User
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/add")
    public Object addUser(User param, Integer[] roleIds) {
        User user = param.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername, param.getUsername()));
        if (user!=null){
            return Result.fail("用户名已存在");
        }
        try{
            if (userService.save(param, roleIds)){
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
