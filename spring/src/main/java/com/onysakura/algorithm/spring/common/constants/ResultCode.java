package com.onysakura.algorithm.spring.common.constants;

public enum ResultCode {

    OK(0, "成功"),
    FAIL(-1, "失败"),
    PARAM_MISSING(1002, "参数缺失");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

