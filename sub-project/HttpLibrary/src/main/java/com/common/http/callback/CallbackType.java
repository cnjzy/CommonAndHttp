package com.common.http.callback;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author jzy
 * created at 2018/5/26
 */
public abstract class CallbackType<T> {

    private Type mType = null;

    public CallbackType() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("泛型异常");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public Type getType() {
        if (mType == null) {
            mType = new TypeToken<String>() {}.getType();
        }
        return mType;
    }
}
