package com.kb.spring.security.service;

import com.kb.spring.security.service.prop.JwtProp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class JwtService {

    @Autowired
    private JwtProp jwtProp;

    public String getJwtStr(String name, Consumer<Map<String, Object>> paramsConsumer) {
        Map<String, Object> params = new HashMap<>();
        paramsConsumer.accept(params);

        Date expirationTime = new Date(System.currentTimeMillis() + jwtProp.getExpirationTime());
        params.put(jwtProp.getExpirationString(), new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(expirationTime));

        return Jwts.builder()
                .setClaims(params)
                .setSubject(name)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, jwtProp.getSecurityCode())
                .compact();
    }

    public Claims getJwtClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProp.getSecurityCode())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getAuthoritiesKey() {
        return jwtProp.getAuthoritiesKey();
    }

    public String getSessionKey() {
        return jwtProp.getSessionKey();
    }
}
