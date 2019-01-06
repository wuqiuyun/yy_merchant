package com.yiyue.store.component.exception;

/**
 * 自定义异常
 * <p>
 * Created by zm on 2018/9/11.
 */
public class YLError extends RuntimeException{
    private int code;
    private String message;

    public YLError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
