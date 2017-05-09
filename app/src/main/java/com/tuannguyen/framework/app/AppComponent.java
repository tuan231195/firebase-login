package com.tuannguyen.framework.app;


import com.tuannguyen.activities.authorization.AuthActivity;
import com.tuannguyen.activities.authorization.login.LoginPresenter;
import com.tuannguyen.activities.authorization.signup.SignupPresenter;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(SignupPresenter signupPresenter);

    void inject(LoginPresenter loginPresenter);

    void inject(AuthActivity authActivity);
}