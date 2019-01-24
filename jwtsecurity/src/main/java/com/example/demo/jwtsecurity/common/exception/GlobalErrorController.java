package com.example.demo.jwtsecurity.common.exception;


import com.example.demo.jwtsecurity.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 全局异常信息处理类
 * 原文：https://www.jianshu.com/p/3998ea8b53a8
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2018-12-07
 */
@Slf4j
@Controller
@RequestMapping("${server.error.path:/error}")
public class GlobalErrorController implements ErrorController {

    @Autowired
    private ErrorInfoBuilder errorInfoBuilder;

    /**
     * 返回详细的错误信息(JSON).
     */
    @RequestMapping
    @ResponseBody
    public Result error(HttpServletRequest request) {
        ErrorInfo errorInfo = errorInfoBuilder.getErrorInfo(request);
        if (errorInfo.getStatus()==403){
            return Result.fail(errorInfo.getStatus(), "无权限");
        }
        return Result.fail(errorInfo.getStatus(), errorInfo.getError());
    }

    @Override
    public String getErrorPath() {//获取映射路径
        return errorInfoBuilder.getErrorProperties().getPath();
    }
}