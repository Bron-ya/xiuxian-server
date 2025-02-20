package com.xx.xiuxianserver.Security.Login;

import cn.hutool.core.lang.UUID;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Result.ResponseResult;
import com.xx.xiuxianserver.Common.Utils.MyJsonUtils;
import com.xx.xiuxianserver.Common.Utils.MyJwtUtilsService;
import com.xx.xiuxianserver.Common.Utils.MyTimeUtils;
import com.xx.xiuxianserver.Entity.GlobalJWTPayload;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户认证成功响应处理器（进行封装 token 对象返回）
 * @author jiangzk
 * 2025/1/24  上午8:46
 */
@Component
public class LoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {

    @Resource
    private MyJwtUtilsService myJwtUtilsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException{

        // 1.获取当前认证成功的用户信息，比对返回的类型
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof GlobalJWTPayload globalJwtPayload)) {
            throw new MyException(HttpStatus.BAD_REQUEST.value(),
                    "登陆认证成功后，authentication.getPrincipal()返回的Object对象必须是：GlobalJWTPayload！");
        }
        // 1.1 随机生成当前用户的会话 id，具有唯一性
        globalJwtPayload.setSessionId(UUID.randomUUID().toString());

        // 2.生成 token 以及 refreshToken
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("token", generateToken(globalJwtPayload));
        responseData.put("refreshToken", generateRefreshToken(globalJwtPayload));
        // 2.1 一些特殊的登录参数，比如三方登录(QQ、微信、gitee、github)，需要额外返回一个字段是否需要跳转的绑定已有账号页面
        Object details = authentication.getDetails();
        if (details instanceof Map detailsMap) {
            responseData.putAll(detailsMap);
        }

        // 2.设置响应的内容类型和字符集
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        // 2.1 返回自定义的 JSON 响应信息
        try (PrintWriter writer = response.getWriter()) {
            writer.print(MyJsonUtils.stringify(ResponseResult.okResult(responseData, "登录成功")));
        }
    }

    /**
     * 设置当认证成功后，不会触发任何重定向操作，因为是基于前后端分离
     */
    public LoginSuccessHandler() {
        this.setRedirectStrategy((request, response, url) -> {
            // 更改重定向策略，前后端分离项目，后端使用RestFul风格，无需做重定向
            // Do nothing, no redirects in REST
        });
    }

    /**
     * 获取当前认证成功用户的访问令牌
     */
    public String generateToken(GlobalJWTPayload globalJwtPayload) {
        // 15分钟后过期
        long expiredTime = MyTimeUtils.nowMilli() + TimeUnit.MINUTES.toMillis(15);
        globalJwtPayload.setExpiredTime(expiredTime);
        return myJwtUtilsService.createJwt(globalJwtPayload, expiredTime);
    }

    /**
     * 获取当前认证成功用户的刷新令牌
     */
    private String generateRefreshToken(GlobalJWTPayload globalJwtPayload) {
        return myJwtUtilsService.createJwt(globalJwtPayload, MyTimeUtils.nowMilli() + TimeUnit.DAYS.toMillis(30));
    }
}
