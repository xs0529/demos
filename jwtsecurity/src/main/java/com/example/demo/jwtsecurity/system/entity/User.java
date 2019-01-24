package com.example.demo.jwtsecurity.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        /**
     * 用户名
     */
         @TableField("username")
    private String username;

        /**
     * 密码
     */
         @TableField("password")
    private String password;

        /**
     * 是否可用（1是0否）
     */
         @TableField("is_usable")
    private Integer isUsable;

        /**
     * 最后登录时间
     */
         @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

        /**
     * 创建时间
     */
         @TableField("create_time")
    private LocalDateTime createTime;

        /**
     * 修改时间
     */
         @TableField("modify_time")
    private LocalDateTime modifyTime;

        /**
     * 盐值
     */
         @TableField("salt")
    private String salt;

        /**
     * 昵称
     */
         @TableField("name")
    private String name;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
