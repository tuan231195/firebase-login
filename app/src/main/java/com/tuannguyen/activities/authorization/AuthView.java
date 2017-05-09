package com.tuannguyen.activities.authorization;


import android.util.SparseArray;
import android.widget.Button;
import com.tuannguyen.framework.app.IsActivity;
import com.tuannguyen.framework.base.BaseView;
import com.tuannguyen.framework.validation.ValidateEditText;

import java.util.List;

public interface AuthView extends BaseView, IsActivity {
    void enableBtn();

    void disableBtn();

    void showDialog();

    void hideDialog();

    void goToMain();

    void displayErrors(SparseArray<Error> errors);

    void clearErrors();

    Button getActionBtn();

    List<ValidateEditText> getControls();

    void showError(String message);
}
