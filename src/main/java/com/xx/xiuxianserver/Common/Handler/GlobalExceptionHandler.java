package com.xx.xiuxianserver.Common.Handler;


import cn.hutool.core.exceptions.ExceptionUtil;

import com.xx.xiuxianserver.Common.Enums.AppHttpCodeEnum;
import com.xx.xiuxianserver.Common.Exception.MyException;
import com.xx.xiuxianserver.Common.Result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

/**
 * 全局异常处理类
 * @author jiangzk
 */
@Slf4j
@RestControllerAdvice("cn.jzk")
public class GlobalExceptionHandler {

    /**
     * 访问权限异常处理方法
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Object accessDeniedException(AccessDeniedException e, HandlerMethod handlerMethod) {
        return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH.getCode(),
                AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
    }

    /**
     * 自定义异常处理方法
     */
    @ExceptionHandler(value = MyException.class)
    public Object myException(MyException e, HandlerMethod handlerMethod) {
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    /**
     * 服务器异常处理方法
     */
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception e, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        log.error("{}:服务器发生异常，异常信息为:", method.getName(), e);
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),
                ExceptionUtil.getMessage(e) + " at " + stackTraceElements[0].toString());
    }
}