package com.dale.net.exception;

/**
 * create by Dale
 * create on 2019/7/12
 * description:
 */
public class ErrorMessage {

    /**
     * 错误状态码,http状态码 或者 服务器返回的状态码
     */
    private int code;

    /**
     * 错误的信息 可能为空
     */
    private String message;


    public ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
