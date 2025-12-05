package com.jvictornascimento.accessmanager.service.utils.validation;

import com.jvictornascimento.accessmanager.service.exceptions.PasswordValidationException;
import org.springframework.stereotype.Component;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.USER_PASSWORD_VALID;

@Component
public class VerifyLength implements IValidatePassword {
    @Override
    public void valid(String password) {
        if (password.length() < 8){
            throw new PasswordValidationException(USER_PASSWORD_VALID.params("Minimum length 8 characters").getMassage());
        }
    }
}
