package com.common.http.interfaces;

/**
 * @author jzy
 * created at 2018/5/28
 */
public interface IApiCode {
    int HTTP_CODE_SUCCES = 200;
    int HTTP_CODE_FAIL_EMPTY = 201;
    int HTTP_CODE_FAIL_SERVER = 201;

    boolean isOk();
}
