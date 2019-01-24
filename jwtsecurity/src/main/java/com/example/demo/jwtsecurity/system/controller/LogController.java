package com.example.demo.jwtsecurity.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.jwtsecurity.common.vo.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.jwtsecurity.system.service.LogService;
import com.example.demo.jwtsecurity.system.entity.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *  @description : Log 控制器
 *  ---------------------------------
 *   @author 谢霜
 *  @since 2019-01-07
 */
@RestController
@RequestMapping("/log")
public class LogController {
    private final Logger logger = LoggerFactory.getLogger(LogController.class);
    @Autowired
    private LogService logService;

    /**
     * @description : 获取分页列表
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/list")
    public Object getLogList(Log param ,@RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "size",defaultValue = "10") Integer size) {
        Page<Log> logpage = new Page<>(page,size);
        return logService.page(logpage,null);
    }

    /**
     * @description : 通过id获取Log
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @GetMapping("/get/{id}")
    public Result getLogById(@PathVariable(value = "id") String id) {
        try {
            Log param= logService.getById(id);
            return Result.ok(param);
        } catch (Exception e) {
            logger.info("异常信息:{}"+e.getMessage());
            return Result.fail("获取信息失败");
        }
    }

    /**
     * @description : 通过id删除Log
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/delete/{id}")
    public Result deleteLogById(@PathVariable(value = "id") String id) {
        List<Integer> list = Arrays.asList(id.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        try{
            if (logService.removeByIds(list)){
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
     * @description : 通过id更新Log
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/update")
    public Object updateLogById(Log param) {
        try{
            if (logService.updateById(param)){
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
     * @description : 添加Log
     * ---------------------------------
     * @author : 谢霜
     * @since : Create in 2019-01-07
     */
    @PostMapping("/add")
    public Object addLog(Log param) {
        try{
            if (logService.save(param)){
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
