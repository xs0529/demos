package com.example.demo.jwtsecurity.common.exception;

import lombok.Data;

/**
 * <p>
 *  异常信息对象
 * </p>
 *
 * @author XieShuang
 * @version v1.0
 * @since 2018-12-07
 */
@Data
public class ErrorInfo {

    /**
     * 发生时间
     */
    private String timestamps;
    /**
     * 访问Url
     */
    private String url;
    /**
     * 错误类型
     */
    private String error;
    /**
     * 错误的堆栈轨迹
     */
    private String stackTrace;
    /**
     * 状态码
     */
    private int status;
    /**
     * 状态码
     */
    private String reasonPhrase;
}
