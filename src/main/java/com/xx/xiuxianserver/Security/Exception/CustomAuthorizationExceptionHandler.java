package com.xx.xiuxianserver.Security.Exception;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 【请求未授权接口】的响应处理类
 * @author jiangzk
 */
public class CustomAuthorizationExceptionHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException){
      throw new MyException(AppHttpCodeEnum.NO_OPERATOR_AUTH.getCode(),
              AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
  }
}
