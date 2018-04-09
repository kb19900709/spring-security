package com.kb.spring.security.security.handler;

import com.kb.spring.security.security.bean.AuthStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SessionExpiredHandler implements SessionInformationExpiredStrategy {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        AuthStatus authStatus = AuthStatus.initAuthStatus(AuthStatus.DEFAULT_ERROR_MESSAGE, SessionAuthenticationException.class.getName());
        response.getOutputStream().println(mapper.writeValueAsString(authStatus));
    }
}
