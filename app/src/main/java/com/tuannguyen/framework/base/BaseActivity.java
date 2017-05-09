package com.tuannguyen.framework.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import com.tuannguyen.framework.app.AppComponent;
import com.tuannguyen.framework.app.SaferApplication;

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setupUi();
        getPresenter().onBind();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = ((SaferApplication) getApplication()).getAppComponent();
        injectComponent(appComponent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onUnbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    protected abstract void setupUi();

    protected abstract void injectComponent(AppComponent component);

    protected abstract BaseActivityPresenter getPresenter();
}
