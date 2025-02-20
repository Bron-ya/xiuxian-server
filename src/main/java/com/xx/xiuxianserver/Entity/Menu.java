package com.xx.xiuxianserver.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 菜单表
 * @author jiangzk
 * 2025/1/24  下午2:59
 */
@Schema(description = "菜单表")
@Data
public class Menu implements Serializable {
    /**
     * 菜单id（权限id）
     */
    @Schema(description = "菜单id（权限id）")
    private Integer id;

    /**
     * 菜单名称（权限名称）
     */
    @Schema(description = "菜单名称（权限名称）")
    private String name;

    /**
     * 角色权限标识
     */
    @Schema(description = "角色权限标识")
    private String permit;

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