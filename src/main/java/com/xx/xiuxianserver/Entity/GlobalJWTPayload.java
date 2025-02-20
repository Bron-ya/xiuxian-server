package com.xx.xiuxianserver.Entity;

import lombok.Data;

/**
 * 处理认证用户 token 信息的类
 *
 * @author jiangzk
 * 2025/1/25 10:54
 */
@Data
public class GlobalJWTPayload {

    private Integer userId;

    private Integer roleId;

    // 会话id，全局唯一，为之后生成 token 做准备
    private String sessionId;

    // 用户过期时间
    private Long expiredTime;
}
