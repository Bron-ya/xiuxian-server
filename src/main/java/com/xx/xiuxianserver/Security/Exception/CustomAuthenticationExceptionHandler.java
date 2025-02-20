package com.xx.xiuxianserver.Security.Exception;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Result.ResponseResult;
import com.xx.xiuxianserver.Common.Utils.MyJsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 【未登录认证情况下请求需认证的接口】响应处理器
 * @author jiangzk
 */
public class CustomAuthenticationExceptionHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

      // 设置响应的内容类型和字符集
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      // 返回自定义的 JSON 错误信息
      try (PrintWriter writer = response.getWriter()) {
          writer.print(MyJsonUtils.stringify(
                  ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(),
                          AppHttpCodeEnum.NEED_LOGIN.getMsg())));
      }
  }
}
