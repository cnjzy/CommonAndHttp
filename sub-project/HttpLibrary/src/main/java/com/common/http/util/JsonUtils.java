package com.common.http.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by jzy on 2017/5/31.
 */

public class JsonUtils {
    private synchronized static Object parser(Type type, String json) {
        try {
            if (json == null || "".equals(json.trim()))
                return null;
            Gson gson = new Gson();
            Object result = gson.fromJson(json, type);
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public synchronized static Object parser(Class cls, String json) {
        try {
            if (json == null || "".equals(json.trim()))
                return null;
            Gson gson = new Gson();
            Object result = gson.fromJson(json, cls);
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public synchronized static Object parser(TypeToken<?> token, String json) {
        return parser(token.getType(), json);
    }

    public synchronized static String toJson(Object vo) {
        try {
            if (vo != null) {
                Gson gson = new Gson();
                return gson.toJson(vo);
            }
        } catch (Exception e) {
        }
        return "";
    }
}
