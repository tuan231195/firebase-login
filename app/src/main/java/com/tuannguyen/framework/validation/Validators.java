package com.tuannguyen.framework.validation;

public class Validators {
    public static final Validator EMPTY = new Validator() {
        @Override
        public Error validate(String controlName, String text) {
            if (text == null || text.isEmpty()) {
                return new Error(controlName + " must not be empty");
            }
            return null;
        }
    };
}
