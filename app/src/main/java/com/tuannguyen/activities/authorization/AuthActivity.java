package com.tuannguyen.activities.authorization;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import com.tuannguyen.framework.app.AppComponent;
import com.tuannguyen.framework.base.BaseActivity;
import com.tuannguyen.loginapp.R;
import com.tuannguyen.activities.safer.MainActivity;

public abstract class AuthActivity extends BaseActivity implements AuthView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void enableBtn() {
        getActionBtn().setEnabled(true);
    }

    @Override
    public void disableBtn() {
        getActionBtn().setEnabled(false);
    }

    @Override
    public void showDialog() {
        mProgressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Authenticating...");
        mProgressDialog.show();
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    @Override
    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void displayErrors(SparseArray<Error> errors) {
        clearErrors();
        for (int i = 0; i < errors.size(); i++) {
            View view = ButterKnife.findById(this, errors.keyAt(i));
            if (view != null && view instanceof EditText) {
                ((EditText) view).setError(errors.get(errors.keyAt(i)).getMessage());
            }
        }
    }

    @Override
    public void clearErrors() {
        for (EditText control : getControls()) {
            control.setError(null);
        }
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }
}
