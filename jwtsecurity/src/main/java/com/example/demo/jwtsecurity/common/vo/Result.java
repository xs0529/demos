package com.example.demo.jwtsecurity.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 谢霜
 * @Date: 2018/09/12 下午 16:32
 * @Description:
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T data;


    public static <T> Result <T> ok(String message, T data){
        Result result = new Result();
        result.setCode(200);
        result.setMessage(message);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> Result <T> ok(String message){
        return ok(message,null);
    }

    public static <T> Result <T> ok(T data){
        return ok("操作成功", data);
    }

    public static <T> Result <T> ok(){
        return ok("操作成功");
    }


    public static <T> Result <T> fail(Integer code, String message, T data){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        result.setData(data);
        return result;
    }

    public static <T> Result <T> fail(String message){
        return fail(message,null);
    }

    public static <T> Result <T> fail(Integer code, String message){
        return fail(code, message,null);
    }

    public static <T> Result <T> fail(String message, T data){
        return fail(500, message, data);
    }

    public static <T> Result <T> fail(T data){
        return fail("操作成功", data);
    }

    public static <T> Result <T> fail(){
        return fail("操作失败");
    }
}
