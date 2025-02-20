package com.xx.xiuxianserver.Common.Enums;

import cn.hutool.core.util.ObjectUtil;

import java.util.EnumSet;

/**
 * 枚举通用接口
 * @author jiangzk
 * 2025/1/21  上午9:48
 */
public interface IBaseEnum<T> {

    T getValue();

    String getLabel();

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @param clazz 类
     * @param <E>   枚举
     * @return enum
     */
    static <E extends Enum<E> & IBaseEnum> E getEnumByValue(Object value, Class<E> clazz) {

        if (value == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        return allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据值获取文本
     *
     * @param value 值
     * @param clazz 类
     * @param <E>   枚举
     * @return 文本
     */
    static <E extends Enum<E> & IBaseEnum> String getLabelByValue(Object value, Class<E> clazz) {
        if (value == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getValue(), value))
                .findFirst()
                .orElse(null);

        String label = null;
        if (matchEnum != null) {
            label = matchEnum.getLabel();
        }
        return label;
    }

    /**
     * 根据文本标签获取值
     *
     * @param label 文本
     * @param clazz 类
     * @param <E>   枚举
     * @return 值
     */
    static <E extends Enum<E> & IBaseEnum> Object getValueByLabel(String label, Class<E> clazz) {
        if (label == null) {
            return null;
        }

        // 获取类型下的所有枚举
        EnumSet<E> allEnums = EnumSet.allOf(clazz);
        E matchEnum = allEnums.stream()
                .filter(e -> ObjectUtil.equal(e.getLabel(), label))
                .findFirst()
                .orElse(null);

        Object value = null;
        if (matchEnum != null) {
            value = matchEnum.getValue();
        }
        return value;
    }
}
