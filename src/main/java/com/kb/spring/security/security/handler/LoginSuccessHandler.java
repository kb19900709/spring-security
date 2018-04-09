package com.kb.spring.security.security.handler;


import com.kb.spring.security.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String jwt = tokenService.getJwt(authentication);
        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, jwt);
        httpServletRequest.getRequestDispatcher("auth/login/success").forward(httpServletRequest, httpServletResponse);
    }
}
