package com.xx.xiuxianserver.Common.Enums;

import lombok.Getter;

/**
 * 这里使用枚举类来实现 ResponseResult 响应结果的统一封装
 * @author jiangzk
 * 2025/1/23  下午3:13
 */
@Getter
public enum AppHttpCodeEnum {

    // 成功
    SUCCESS(200,"操作成功"),

    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    USER_NOT_FOUND(404,"当前用户不存在"),
    LOGGED_IN_ELSEWHERE(409,"当前用户已在其他地方登录"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    PHONE_OR_CODE_ERROR(506,"手机号或验证码错误"),
    COMMENT_NOT_NULL(507,"内容不能为空!"),
    FILE_TYPE_ERROR(508, "文件的类型只能为 png 或者 jpg !"),
    INVALID_USERNAME_OR_PASSWORD(509,"用户名或密码不能为空!"),
    INVALID_PHONE_OR_CODE(510,"手机号或验证码不能为空!"),
    NICKNAME_NOT_NULL(511,"用户昵称不能为空!"),
    EMAIL_NOT_NULL(512,"用户邮箱不能为空!"),
    NICKNAME_EXIST(513,"昵称已存在!"),
    MENU_UPDATE_ERROR(514,"修改菜单失败，上级菜单不能选择自己"),
    MENU_DELETE_ERROR(515,"存在子菜单不允许删除");

    final int code;
    final String msg;
    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }
}
