package com.tuannguyen.framework.base;


public abstract class BasePresenter<T extends BaseView> {
    protected final String TAG = getClass().getSimpleName();
    private T mBaseView;

    public BasePresenter(T baseView) {
        this.mBaseView = baseView;
    }

    protected void onBind() {}

    protected void onUnbind(){}

    protected T view() {
        return mBaseView;
    }
}
