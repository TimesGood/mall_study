package com.example.mall_study.exception;

import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {
    public CaptchaException(String message) {
        super(message);
    }
}
