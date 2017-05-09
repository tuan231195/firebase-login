package com.tuannguyen.activities.authorization.signup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.tuannguyen.activities.authorization.AuthPresenter;
import com.tuannguyen.activities.authorization.AuthView;
import com.tuannguyen.model.User;

import javax.inject.Inject;

public class SignupPresenter extends AuthPresenter {


    @Inject
    FirebaseDatabase mFirebaseDatabase;

    @Inject
    Context mContext;

    public SignupPresenter(AuthView baseView) {
        super(baseView);
    }

    @Override
    protected void onBind() {
    }

    public void signup(final String name, final String phone, final String email, String password) {
        if (validate()) {
            toggleBtnAndDialog(true);
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(view().asActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            toggleBtnAndDialog(false);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                saveUser(name, email, phone);
                                view().goToMain();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                view().showError(task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private void saveUser(String name, String email, String phone) {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "saveUser:failure: User cannot be null");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUid(firebaseUser.getUid());
        user.setPhoneNumber(phone);

        saveUser(user);
    }
}
