package com.eazybytes.eazyschool.validations;

import com.eazybytes.eazyschool.annotation.PasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
//contraintValidator is inbuit interface we implementing

public class PasswordStrengthValidator implements
        ConstraintValidator<PasswordValidator, String> {

    List<String> weakPasswords;

    @Override
    public void initialize(PasswordValidator passwordValidator) {
        weakPasswords = Arrays.asList("12345", "password", "qwerty");
    }

    @Override
    //this one check password is not null and password not contain weak password
    //inbuilt interface "constrain.Vali" present in javax library
    public boolean isValid(String passwordField,
                           ConstraintValidatorContext cxt) {
        return passwordField != null && (!weakPasswords.contains(passwordField));
    }
}
