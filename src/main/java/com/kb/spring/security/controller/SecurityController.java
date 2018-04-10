package com.kb.spring.security.controller;

import com.kb.spring.security.security.bean.AuthStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class SecurityController extends BaseController {

    @PostMapping("login/success")
    public AuthStatus loginSuccess() {
        return AuthStatus.initAuthStatus("Login success");
    }

    @PostMapping("login/fail")
    public AuthStatus loginFail() {
        Object exception = httpRequest.getAttribute(AuthStatus.DEFAULT_AUTH_EXCEPTION);
        if (exception != null) {
            AuthenticationException authException = (AuthenticationException) exception;
            return AuthStatus.initAuthStatus(authException.getMessage(), authException.getClass().getName());
        }
        return AuthStatus.initAuthStatus("Login fail, please try again");
    }

    @PostMapping("login/out")
    public AuthStatus logout() {
        return AuthStatus.initAuthStatus("Logout success");
    }
}
