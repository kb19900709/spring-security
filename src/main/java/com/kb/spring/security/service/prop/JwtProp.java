package com.kb.spring.security.service.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
public class JwtProp {
    private String authoritiesKey;
    private long expirationTime;
    private String expirationString;
    private String headerKey;
    private String securityCode;
    private String sessionKey;

    public String getAuthoritiesKey() {
        return authoritiesKey;
    }

    public void setAuthoritiesKey(String authoritiesKey) {
        this.authoritiesKey = authoritiesKey;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getExpirationString() {
        return expirationString;
    }

    public void setExpirationString(String expirationString) {
        this.expirationString = expirationString;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
