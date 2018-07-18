package com.common.http.bean;

import com.google.gson.Gson;
import com.library.bean.BaseBean;

import java.io.Serializable;

/**
 * @author jzy
 * created at 2018/5/28
 * 请求类基类，所有请求类必须继承此类
 */
public abstract class BaseReq extends BaseBean {

    public abstract String getUrlPath();
}
