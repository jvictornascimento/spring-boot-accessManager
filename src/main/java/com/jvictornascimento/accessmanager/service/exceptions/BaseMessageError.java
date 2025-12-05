package com.jvictornascimento.accessmanager.service.exceptions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.text.MessageFormat;
import java.util.ResourceBundle;


@RequiredArgsConstructor

public class BaseMessageError {
    private final String DEFAULT_RESOURCE = "messages";

    private final String key;
    private String [] params;

    public static final BaseMessageError GENERIC_EXCEPTION = new BaseMessageError("generic");
    public static final BaseMessageError GENERIC_METHOD_NOT_ALLOW = new BaseMessageError("generic.methodNotAllow");
    public static final BaseMessageError USER_NOT_FOUND = new BaseMessageError("user.notFound");
    public static final BaseMessageError USER_NOT_FOUND_BY_ID = new BaseMessageError("user.notFound.id");
    public static final BaseMessageError USER_EMAIL_ALREADY_EXISTS = new BaseMessageError("user.emailAlreadyExists");
    public static final BaseMessageError USER_REQUIRED_FIELDS = new BaseMessageError("user.requiredField");
    public static final BaseMessageError USER_PASSWORD = new BaseMessageError("user.password");
    public static final BaseMessageError USER_PASSWORD_VALID = new BaseMessageError("user.password.valid");

    public BaseMessageError params(final String ... params) {
        this.params = ArrayUtils.clone(params);
        return this;
    }
    public String getMassage() {
        var message = tryGetMessageFromBundle();
        if(ArrayUtils.isNotEmpty(params)) {
            final var fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }
    public String tryGetMessageFromBundle() {
        return gerResource().getString(key);
    }

    public ResourceBundle gerResource() {
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}
