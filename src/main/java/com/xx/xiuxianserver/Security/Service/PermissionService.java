package com.xx.xiuxianserver.Security.Service;

import cn.hutool.core.util.StrUtil;
import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Utils.SecurityUtils;
import com.xx.xiuxianserver.Security.Login.UserName.UserNameAuthentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 权限校验处理类
 * @author jiangzk
 * 2025/1/26  下午5:24
 */
@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否有当前传入的权限
     * @param requiredPerm 当前接口所需要的权限
     * @return 是否有权限
     */
    public boolean hasPerm(String requiredPerm) {

        if (StrUtil.isBlank(requiredPerm)) {
            return false;
        }

        // 1.获取当前用户的权限集合
        UserNameAuthentication loginUser = SecurityUtils.getLoginUser();
        Collection<? extends GrantedAuthority> roles = loginUser.getAuthorities();
        if (roles == null || roles.isEmpty()) {
            return false;
        }

        // 2.判断当前用户权限集合是否有传入的权限
        boolean hasPermission = roles.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredPerm::equals);
        if (!hasPermission) {
            throw new MyException(AppHttpCodeEnum.NO_OPERATOR_AUTH.getCode(),
                    AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
        }
        return true;
    }
}
