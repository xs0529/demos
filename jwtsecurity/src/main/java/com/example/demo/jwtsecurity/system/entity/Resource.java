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
@TableName("sys_resource")
public class Resource extends Model<Resource> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        /**
     * 权限名称
     */
         @TableField("name")
    private String name;

        /**
     * 父id
     */
         @TableField("parent_id")
    private String parentId;

        /**
     * 类型（0菜单1按钮）
     */
         @TableField("type")
    private Integer type;

        /**
     * 访问路径
     */
         @TableField("url")
    private String url;

        /**
     * 权限标识
     */
         @TableField("permission")
    private String permission;

        /**
     * 颜色
     */
         @TableField("color")
    private String color;

        /**
     * 图标
     */
         @TableField("icon")
    private String icon;

        /**
     * 排序
     */
         @TableField("sort")
    private Integer sort;

        /**
     * 验证与否（0不验证，1验证，2，无法访问）
     */
         @TableField("verification")
    private Integer verification;

        /**
     * 创建时间
     */
         @TableField("create_date")
    private LocalDateTime createDate;

        /**
     * 日志是否保存（1是0否）
     */
         @TableField("log_is_save")
    private Boolean logIsSave;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
