package com.xx.xiuxianserver.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @author jiangzk
 * 2025/1/24  下午5:09
 */
@Schema(description = "用户信息表")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @Schema(description = "id")
    private Integer id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 用户类型 1：管理员；99：用户
     */
    @Schema(description = "用户类型 1：管理员；99：用户")
    private Integer type;

    /**
     * 操作者id
     */
    @Schema(description = "操作者id")
    private Integer operatorId;

    /**
     * 用户状态（0：未启用，1：启用，默认1）
     */
    @Schema(description = "用户状态（0：未启用，1：启用，默认1）")
    private Boolean status;

    /**
     * 是否删除（0：未删除，1：删除，默认0）
     */
    @Schema(description = "是否删除（0：未删除，1：删除，默认0）")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 编辑时间
     */
    @Schema(description = "编辑时间")
    private Date editTime;

    @Serial
    private static final long serialVersionUID = 1L;
}