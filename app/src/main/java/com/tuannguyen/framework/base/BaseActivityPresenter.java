package com.tuannguyen.framework.base;


import android.content.Intent;
import android.util.Log;

public abstract class BaseActivityPresenter<T extends BaseView> extends BasePresenter<T>{
    public BaseActivityPresenter(T baseView) {
        super(baseView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "OnActivityResult:" + requestCode + ", " + resultCode + ", " + data);
    }
}
