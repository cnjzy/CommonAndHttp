package com.base.ui.presenter;

/**
 * Created by apple on 2017/12/7.
 */

public class PresenterFactory<P extends IBaseMvpPresenter> {
    private Class<P> presenterClass;

    public static <P extends IBaseMvpPresenter> PresenterFactory bind(Class<?> view) {
        Presenter annotation = view.getAnnotation(Presenter.class);
        Class<P> clazz = null;
        if (annotation != null) {
            clazz = (Class<P>) annotation.value();
        }
        return new PresenterFactory(clazz);
    }

    private PresenterFactory(Class<P> presenterClass) {
        this.presenterClass = presenterClass;
    }

    public P create() {
        try {
            return presenterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Presenter创建失败!，检查是否声明了@CreatePresenter(xx.class)注解");
        }
    }
}
