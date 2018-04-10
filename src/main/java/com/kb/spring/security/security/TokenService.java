package com.kb.spring.security.security;


import com.kb.spring.security.security.bean.CurrentUser;
import com.kb.spring.security.service.JwtService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@Component
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CurrentUser currentUser;

    public String getJwt() {
        String name = currentUser.getUserName();
        String roles = Optional.ofNullable(currentUser.getAuthorities()).orElseGet(ArrayList::new).stream()
                .map(GrantedAuthority::getAuthority)
                .collect(joining(","));
        String sessionId = currentUser.getSessionId();

        if (name != null && roles != null && sessionId != null) {
            return jwtService.getJwtStr(name, params -> {
                params.put(jwtService.getAuthoritiesKey(), roles);
                params.put(jwtService.getSessionKey(), sessionId);
            });
        }

        return null;
    }

    public Authentication getAuthentication() {
        String name = currentUser.getUserName();
        List<GrantedAuthority> authorities = currentUser.getAuthorities();
        return name != null ? new UsernamePasswordAuthenticationToken(name, null, authorities) : null;
    }

    public void initCurrentUser(String jwtStr) {
        try {
            Claims claims = jwtService.getJwtClaims(jwtStr);
            String name = claims.getSubject();
            String sessionId = (String) claims.get(jwtService.getSessionKey());
            List<GrantedAuthority> authorities =
                    Optional.ofNullable(AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(jwtService.getAuthoritiesKey())))
                            .orElseGet(ArrayList::new);

            currentUser.setUserName(name);
            currentUser.setSessionId(sessionId);
            currentUser.setAuthorities(authorities);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            currentUser.setError(new BadCredentialsException(String.format("JWT parsed error, maybe it's an illegal authorization: %s", jwtStr)));
        }
    }
}
