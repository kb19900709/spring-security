package com.kb.spring.security.security.handler;

import com.kb.spring.security.security.TokenService;
import com.kb.spring.security.security.bean.CurrentUser;
import com.kb.spring.security.service.prop.JwtProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class SessionStrategyHandler extends HeaderHttpSessionStrategy {

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtProp jwtProp;

    @Override
    public String getRequestedSessionId(HttpServletRequest request) {
        String result = Optional.ofNullable(currentUser.getSessionId()).orElse("");
        if (!result.isEmpty()) {
            return result;
        }

        if (!request.getServletPath().contains("login")) {
            String headerValue = request.getHeader(getSpecificHeader());
            Optional.ofNullable(headerValue).ifPresent(tokenService::initCurrentUser);
        }

        return currentUser.getSessionId();
    }

    @Override
    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        if (currentUser.getSessionId() == null) {
            currentUser.setSessionId(session.getId());
            String jwt = tokenService.getJwt();
            response.setHeader(getSpecificHeader(), jwt);
        }
    }

    @Override
    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(getSpecificHeader(), "");
    }

    private String getSpecificHeader() {
        return jwtProp.getHeaderKey();
    }
}
