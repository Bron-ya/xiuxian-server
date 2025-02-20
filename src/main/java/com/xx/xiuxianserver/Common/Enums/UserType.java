package com.xx.xiuxianserver.Common.Enums;

import lombok.Getter;

/**
 * 用户类型枚举
 * @author jiangzk
 * 2025/1/21  上午9:49
 */
@Getter
public enum UserType {

    MANAGER(1, "管理员"),
    USER(99, "用户");

    private final Integer value;

    private final String label;

    UserType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
