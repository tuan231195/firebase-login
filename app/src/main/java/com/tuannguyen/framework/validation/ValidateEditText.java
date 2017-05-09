package com.tuannguyen.framework.validation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import com.tuannguyen.loginapp.R;

public class ValidateEditText extends EditText {
    Validator mValidator = Validators.EMPTY;
    String mControlName;

    public ValidateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ValidateEditText,
                0, 0);
        try {
            mControlName = a.getString(R.styleable.ValidateEditText_controlName);
        } finally {
            a.recycle();
        }

    }

    public void setValidator(Validator validator) {
        this.mValidator = validator;
    }

    public Error validate() {
        return mValidator.validate(mControlName, getText().toString());
    }
}
