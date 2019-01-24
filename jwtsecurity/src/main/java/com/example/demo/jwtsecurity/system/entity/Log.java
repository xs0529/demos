package com.example.demo.jwtsecurity.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 谢霜
 * @since 2019-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
public class Log extends Model<Log> {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("username")
    private String username;

    @TableField("uid")
    private String uid;

    @TableField("ip")
    private String ip;

    @TableField("ajax")
    private Integer ajax;

    @TableField("uri")
    private String uri;

    @TableField("params")
    private String params;

    @TableField("http_method")
    private String httpMethod;

    @TableField("class_method")
    private String classMethod;

    @TableField("action_name")
    private String actionName;

    @TableField("create_date")
    private LocalDateTime createDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
