package com.xx.xiuxianserver.Common.Utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间工具类
 * @author jiangzk
 * 2025/1/24  上午10:20
 */
public class MyTimeUtils {

    /**
     * 获取当前日期时间
     */
    public static Date nowDate() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间戳
     */
    public static long nowMilli() {
        return System.currentTimeMillis();
    }
}
