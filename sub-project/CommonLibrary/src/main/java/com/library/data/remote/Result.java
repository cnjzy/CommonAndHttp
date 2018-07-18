package com.library.data.remote;

/**
 * @author jzy
 * created at 2018/6/4
 */
public class Result<T> {
    public static final int STATUS_OK = 200;
    public static final int STATUS_LOCAL_ERROR = -1;
    public static final int STATUS_NO_ENGOUTH_DATA = -2;
    /**
     * status : 400
     * msg : unexpected end of JSON input
     * data : null
     */
    private int code;
    private String msg;
    private T data;
    public Result() {
    }
    public Result(T data) {
        this.data = data;
    }
    public Result(Throwable e) {
        code = STATUS_LOCAL_ERROR;
        msg = e.getMessage();
    }
    public Result(int ret, T data) {
        this.code = ret;
        this.data = data;
    }
    public Result(int ret, String msg, T data) {
        this.code = ret;
        this.msg = msg;
        this.data = data;
    }
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
    public boolean isOk(){
        return code == STATUS_OK;
    }
}
