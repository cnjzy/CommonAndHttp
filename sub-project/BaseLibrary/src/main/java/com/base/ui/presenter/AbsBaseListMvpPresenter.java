package com.base.ui.presenter;

import com.base.contract.DataListContract;
import com.base.ui.view.IBaseMvpView;

/**
 * Created by jzy on 2018/3/23.
 */

public abstract class AbsBaseListMvpPresenter<V extends IBaseMvpView> implements IBaseMvpPresenter<V> {
    private V mView = null;

    public void attach(V view) {
        mView = view;
    }

    public void detach() {
        mView = null;
    }

    public V getView() {
        return mView;
    }
}
