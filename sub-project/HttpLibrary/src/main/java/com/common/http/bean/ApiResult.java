package com.common.http.bean;

import com.common.http.interfaces.IApiCode;

/**
 * @author jzy
 * created at 2018/5/23
 */
public class ApiResult<T> implements IApiCode {
    public static final String CODE_NAME = "code";
    public static final String MSG_NAME = "msg";
    public static final String DATA_NAME = "data";

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        return code == HTTP_CODE_SUCCES;
    }
}
