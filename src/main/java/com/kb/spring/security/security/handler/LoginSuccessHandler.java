package com.kb.spring.security.security.handler;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String userName = authentication.getName();
        httpServletRequest.setAttribute("userName", userName);
        httpServletRequest.getRequestDispatcher("auth/login/success").forward(httpServletRequest, httpServletResponse);
    }
}
