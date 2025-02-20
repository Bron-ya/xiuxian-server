package com.xx.xiuxianserver.Common.Utils;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import com.xx.xiuxianserver.Security.Login.UserName.UserNameAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author jiangzk
 * 2025/1/23  上午9:01
 */
public class SecurityUtils {

    /**
     * 获取 Authentication 对象
     */
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new MyException(AppHttpCodeEnum.NEED_LOGIN.getCode(),
                    AppHttpCodeEnum.NEED_LOGIN.getMsg());
        }
        return authentication;
    }

    /**
     * 获取当前认证成功的用户信息
     **/
    public static UserNameAuthentication getLoginUser(){
        return (UserNameAuthentication)getAuthentication();
    }

    /**
     *  获取当前认证成功的用户ID
     */
    public static Integer getUserId() {
        GlobalJWTPayload globalJwtPayload = getLoginUser().getGlobalJwtPayload();
        if (globalJwtPayload == null) {
            throw new MyException(AppHttpCodeEnum.NEED_LOGIN.getCode(),
                    AppHttpCodeEnum.NEED_LOGIN.getMsg());
        }
        return globalJwtPayload.getUserId();
    }
}
