package com.xx.xiuxianserver.Common.Exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常类
 * @author jiangzk
 */
@Getter
@Setter
public class MyException extends RuntimeException {

    private Integer code;
    private String msg;

    public MyException(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
}