package com.xx.xiuxianserver.Security.Exception;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Result.ResponseResult;
import com.xx.xiuxianserver.Common.Utils.MyJsonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * 捕捉 SpringSecurity FilterChain 中抛出的未知异常
 * @author jiangzk
 */
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {

  public static final String UTF8 = "UTF-8";

  public static final Logger logger = LoggerFactory.getLogger(
      CustomSecurityExceptionHandler.class);

  @Override
  public void doFilterInternal(@NonNull HttpServletRequest request,
                               @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (MyException e) {
      // 自定义异常
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(UTF8);
      response.getWriter().write(Objects.requireNonNull(MyJsonUtils.stringify(
              ResponseResult.errorResult(e.getCode(), e.getMessage()))));
    } catch (AuthenticationException | AccessDeniedException e) {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(UTF8);
      response.getWriter().print(MyJsonUtils.stringify(
              ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH, e.getMessage())));
    } catch (Exception e) {
      // 未知异常
      logger.error(e.getMessage(), e);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(UTF8);
      response.getWriter().write(Objects.requireNonNull(MyJsonUtils.stringify(
              ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, e.getMessage()))));
    }
  }
}