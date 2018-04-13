package com.kb.spring.security.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.spring.security.security.bean.AccountCredentials;
import com.kb.spring.security.security.bean.CurrentUser;
import com.kb.spring.security.security.handler.LoginFailureHandler;
import com.kb.spring.security.security.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CurrentUser currentUser;

    private ObjectMapper mapper = new ObjectMapper();

    public LoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        AccountCredentials accountCredentials =
                Optional.ofNullable(mapper.readValue(httpServletRequest.getInputStream(), AccountCredentials.class))
                        .orElseThrow(() -> new BadCredentialsException("Please input correct data to login"));

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        Optional.ofNullable(accountCredentials.getUserName())
                                .orElseThrow(() -> new BadCredentialsException("Please input userName to login"))
                        , Optional.ofNullable(accountCredentials.getPassword())
                                .orElseThrow(() -> new BadCredentialsException("Please input password to login"))
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        initCurrentUser(authResult);
        loginSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        loginFailureHandler.onAuthenticationFailure(request, response, failed);
    }

    private void initCurrentUser(Authentication authentication) {
        currentUser.setUserName(authentication.getName());
        currentUser.setAuthorities((List<GrantedAuthority>) authentication.getAuthorities());
    }
}
