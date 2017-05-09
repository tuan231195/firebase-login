package com.tuannguyen.activities.authorization;


import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.tuannguyen.framework.base.BaseActivityPresenter;
import com.tuannguyen.framework.validation.ValidateEditText;
import com.tuannguyen.model.User;

import javax.inject.Inject;

public abstract class AuthPresenter extends BaseActivityPresenter<AuthView> {
    @Inject
    protected FirebaseAuth mFirebaseAuth;

    @Inject
    protected FirebaseDatabase mFirebaseDatabase;

    public AuthPresenter(AuthView baseView) {
        super(baseView);
    }

    @Override
    protected void onBind() {
        super.onBind();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            view().goToMain();
        }
    }

    protected boolean validate() {
        boolean isValid = true;
        SparseArray<Error> errors = new SparseArray<>();
        for (ValidateEditText edt : view().getControls()) {
            Error error = edt.validate();
            if (error != null) {
                isValid = false;
                errors.put(edt.getId(), error);
            }
        }

        if (isValid) {
            view().clearErrors();
        } else {
            view().displayErrors(errors);
        }
        return isValid;
    }

    protected void toggleBtnAndDialog(boolean b) {
        if (b) {
            view().showDialog();
            view().disableBtn();
        }
        else {
            view().enableBtn();
            view().hideDialog();
        }
    }

    protected void saveUser(User user) {
        mFirebaseDatabase.getReference().child("users").child(user.getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "saveUser:success");

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "saveUser:failure", task.getException());
                }
            }
        });
    }
}
