package com.kb.spring.security.security.filter;

import com.kb.spring.security.security.TokenService;
import com.kb.spring.security.security.bean.AuthStatus;
import com.kb.spring.security.security.bean.CurrentUser;
import com.kb.spring.security.service.prop.JwtProp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private JwtProp jwtProp;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication;
        try {
            Optional.ofNullable(httpServletRequest.getHeader(jwtProp.getHeaderKey()))
                    .orElseThrow(() -> new BadCredentialsException("There is no JWT in http header"));

            Exception parseError = currentUser.getError();
            if (parseError != null) {
                throw parseError;
            }

            String currentSessionId = Optional.ofNullable(currentUser.getSessionId())
                    .orElseThrow(() -> new SessionAuthenticationException("There is no sessionId in JWT"));

            String theLastSessionId = sessionRegistry.getAllSessions(currentUser.getUserName(), false)
                    .stream()
                    .sorted(getLastSessionInformationComparator().reversed())
                    .findFirst()
                    .map(SessionInformation::getSessionId)
                    .orElse("");

            if (!currentSessionId.equals(theLastSessionId)) {
                throw new SessionAuthenticationException(AuthStatus.DEFAULT_ERROR_MESSAGE);
            }

            String newJwt = tokenService.getJwt();
            httpServletResponse.setHeader(jwtProp.getHeaderKey(), newJwt);
            authentication = tokenService.getAuthentication();
        } catch (Exception e) {
            authentication = null;
            httpServletRequest.setAttribute(AuthStatus.DEFAULT_AUTH_EXCEPTION, e);
            LOGGER.error(e.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Comparator<SessionInformation> getLastSessionInformationComparator() {
        return Comparator.comparingLong(
                sessionInformation -> sessionInformation.getLastRequest().getTime()
        );
    }
}
