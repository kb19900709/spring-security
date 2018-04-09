package com.kb.spring.security.security.handler;

import com.kb.spring.security.security.bean.AuthStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Unauthorized";

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public AuthStatus authHandler(AuthenticationException authException) {
        return AuthStatus.initAuthStatus(
                Optional.of(authException)
                        .map(Throwable::getMessage)
                        .orElse(DEFAULT_ERROR_MESSAGE)
                , authException.getClass().getName());
    }
}
