package com.tuannguyen.framework.validation;

public interface Validator {
    Error validate(String controlName, String text);
}
