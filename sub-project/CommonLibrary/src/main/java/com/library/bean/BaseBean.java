package com.library.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by jzy on 2017/7/13.
 */

public class BaseBean implements Serializable {

    @Override
    public final String toString() {
        return new Gson().toJson(this);
    }
}
