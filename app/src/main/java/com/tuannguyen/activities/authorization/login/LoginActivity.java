package com.tuannguyen.activities.authorization.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tuannguyen.activities.authorization.AuthActivity;
import com.tuannguyen.activities.authorization.AuthView;
import com.tuannguyen.activities.authorization.signup.SignupActivity;
import com.tuannguyen.framework.app.AppComponent;
import com.tuannguyen.framework.base.BaseActivity;
import com.tuannguyen.framework.base.BaseActivityPresenter;
import com.tuannguyen.framework.validation.ValidateEditText;
import com.tuannguyen.framework.validation.Validator;
import com.tuannguyen.loginapp.R;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AuthActivity implements AuthView {

    @BindView(R.id.app_banner)
    TextView mTvAppBanner;

    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindView(R.id.input_email)
    ValidateEditText mEdtEmail;

    @BindView(R.id.input_password)
    ValidateEditText mEdtPassword;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLoginPresenter = new LoginPresenter(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public void setupUi() {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/GreatVibes-Regular.otf");
        mTvAppBanner.setTypeface(face);

        mEdtEmail.setValidator(new Validator() {
            @Override
            public Error validate(String controlName, String email) {
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    return new Error("Please enter a valid email");
                }
                return null;
            }
        });

        mEdtPassword.setValidator(new Validator() {
            @Override
            public Error validate(String controlName, String password) {
                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    return new Error("Password must be between 4 and 10 alphanumeric characters");
                }
                return null;
            }
        });
    }

    @OnClick(R.id.btn_login_facebook)
    public void facebookLogin(){
        mLoginPresenter.facebookLogin();
    }

    @OnClick(R.id.btn_login_google)
    public void googleLogin(){
        mLoginPresenter.googleLogin();
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(mLoginPresenter);
    }

    @Override
    protected BaseActivityPresenter<AuthView> getPresenter() {
        return mLoginPresenter;
    }


    @OnClick(R.id.link_signup)
    public void goToSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        String email = mEdtEmail.getText().toString();
        String password = mEdtPassword.getText().toString();
        mLoginPresenter.login(email, password);
    }

    @Override
    public Button getActionBtn() {
        return mBtnLogin;
    }

    @Override
    public List<ValidateEditText> getControls() {
        return Arrays.asList(mEdtEmail, mEdtPassword);
    }


    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public BaseActivity asActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginPresenter.onActivityResult(requestCode, resultCode, data);
    }
}
