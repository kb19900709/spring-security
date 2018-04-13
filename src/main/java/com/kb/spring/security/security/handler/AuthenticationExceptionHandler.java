package com.kb.spring.security.security.handler;

import com.kb.spring.security.security.bean.AuthStatus;
import com.kb.spring.security.security.bean.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Unauthorized";

    @Autowired
    private CurrentUser currentUser;

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public AuthStatus authHandler(AuthenticationException authException) {
        return getAuthStatus(authException);
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public AuthStatus authHandler(AccessDeniedException accessDeniedException) {
        return getAuthStatus(accessDeniedException);
    }

    private AuthStatus getAuthStatus(Exception exception) {
        AuthStatus result = AuthStatus.initAuthStatus(
                Optional.of(exception)
                        .map(Throwable::getMessage)
                        .orElse(DEFAULT_ERROR_MESSAGE)
                , exception.getClass().getName());
        result.setCurrentUserName(currentUser.getUserName());
        return result;
    }
}
