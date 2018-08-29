package com.base.listener;

import android.view.View;

/**
 * Created by jzy on 2017/6/12.
 */

public interface IOnClickListenerIntercept {
    boolean onInterceptClick(View v);

    void addOnClickInterceptId(int id, int type, int source);
}
