package com.xx.xiuxianserver.Common.Enums;

import lombok.Getter;

/**
 * 第三方类型枚举类
 * @author jiangzk
 * 2025/1/26  上午8:52
 */
@Getter
public enum PlayFormType {

    QQ(1, "qq"),
    WECHAT(2, "wechat"),
    GITEE(3, "gitee"),
    GITHUB(4, "github");

    private final Integer value;
    private final String label;

    PlayFormType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
