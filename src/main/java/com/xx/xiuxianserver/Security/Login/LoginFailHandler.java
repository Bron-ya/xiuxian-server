package com.xx.xiuxianserver.Security.Login;


import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Result.ResponseResult;
import com.xx.xiuxianserver.Common.Utils.MyJsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户认证失败响应处理器
 * @author jiangzk
 * 2025/1/24  上午8:45
 */
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{

        // 获取 AuthenticationProvider 传来的认证失败异常信息
        String loginErrorMessage = exception.getMessage();

        // 设置响应的内容类型和字符集
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 返回自定义的 JSON 响应信息
        try (PrintWriter writer = response.getWriter()) {
            writer.print(MyJsonUtils.stringify(
                    ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), loginErrorMessage)));
        }
    }
}
