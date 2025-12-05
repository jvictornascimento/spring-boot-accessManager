package com.jvictornascimento.accessmanager.service.utils.validation;

import com.jvictornascimento.accessmanager.service.exceptions.PasswordValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.USER_PASSWORD_VALID;
@Component
public class verifyExistSpecialChar implements IValidatePassword {
    @Override
    public void valid(String password) {
        Pattern pattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()){
            throw new PasswordValidationException(USER_PASSWORD_VALID.params("At least one special character").getMassage());
        }
    }
}
