package com.kb.spring.security.security;

import com.kb.spring.security.security.bean.AuthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthenticationException finalAuthException = null;
        try {
            finalAuthException = Optional.ofNullable(request.getAttribute(AuthStatus.DEFAULT_AUTH_EXCEPTION))
                    .map(e -> (AuthenticationException) e)
                    .orElse(authException);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            finalAuthException = authException;
        } finally {
            resolver.resolveException(request, response, null, finalAuthException);
        }
    }
}
