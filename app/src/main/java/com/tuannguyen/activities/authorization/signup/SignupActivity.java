package com.tuannguyen.activities.authorization.signup;

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
import com.tuannguyen.activities.authorization.login.LoginActivity;
import com.tuannguyen.framework.app.AppComponent;
import com.tuannguyen.framework.base.BaseActivity;
import com.tuannguyen.framework.base.BaseActivityPresenter;
import com.tuannguyen.framework.validation.ValidateEditText;
import com.tuannguyen.framework.validation.Validator;
import com.tuannguyen.loginapp.R;

import java.util.Arrays;
import java.util.List;

public class SignupActivity extends AuthActivity implements AuthView {

    @BindView(R.id.app_banner)
    TextView mTvAppBanner;

    @BindView(R.id.btn_signup)
    Button mBtnSignup;

    @BindView(R.id.input_name)
    ValidateEditText mEdtName;

    @BindView(R.id.input_email)
    ValidateEditText mEdtEmail;

    @BindView(R.id.input_password)
    ValidateEditText mEdtPassword;

    @BindView(R.id.input_phone)
    ValidateEditText mEdtPhone;

    private SignupPresenter mSignupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSignupPresenter = new SignupPresenter(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    @Override
    protected void setupUi() {
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

        mEdtPhone.setValidator(new Validator() {
            @Override
            public Error validate(String controlName, String phone) {
                if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
                    return new Error("Please enter a valid phone");
                }
                return null;
            }
        });
    }

    @OnClick(R.id.btn_signup)
    public void onClick() {
        mSignupPresenter.signup(mEdtName.getText().toString(), mEdtPhone.getText().toString(), mEdtEmail.getText().toString(), mEdtPassword.getText().toString());
    }

    @OnClick(R.id.link_login)
    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(mSignupPresenter);
    }

    @Override
    protected BaseActivityPresenter<AuthView> getPresenter() {
        return mSignupPresenter;
    }

    @Override
    public Button getActionBtn() {
        return mBtnSignup;
    }

    @Override
    public List<ValidateEditText> getControls() {
        return Arrays.asList(mEdtName, mEdtEmail, mEdtPassword);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public BaseActivity asActivity() {
        return this;
    }
}
