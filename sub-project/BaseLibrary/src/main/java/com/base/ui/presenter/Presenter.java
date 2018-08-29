package com.base.ui.presenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by apple on 2017/12/7.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Presenter {
    Class<? extends IBaseMvpPresenter> value();
}
