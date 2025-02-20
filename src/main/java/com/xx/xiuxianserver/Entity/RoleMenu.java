package com.xx.xiuxianserver.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色与菜单关联表
 * @author jiangzk
 * 2025/1/24  下午3:00
 */
@Schema(description = "角色与菜单关联表")
@Data
public class RoleMenu implements Serializable {
    /**
     * id
     */
    @Schema(description = "id")
    private Integer id;

    /**
     * 角色id
     */
    @Schema(description = "角色id")
    private Integer roleId;

    /**
     * 菜单id
     */
    @Schema(description = "菜单id")
    private Integer menuId;

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