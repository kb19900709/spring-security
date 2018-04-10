package com.kb.spring.security.security;

import com.kb.spring.security.security.bean.GrantedAuthorityImpl;
import com.kb.spring.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (isValid(authentication)) {
            return getNewAuthentication(authentication);
        }
        throw new BadCredentialsException("Password isn't correct");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean isValid(Authentication authentication) {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        return authService.isValid(name, password);
    }

    private Authentication getNewAuthentication(Authentication authentication) {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<GrantedAuthority> rolesList = getAuthorities(name);
        return new UsernamePasswordAuthenticationToken(name, password, rolesList);
    }

    private List<GrantedAuthority> getAuthorities(String name) {
        return authService.findUserAuthorities(name)
                .stream()
                .map(GrantedAuthorityImpl::new)
                .collect(toList());
    }
}
