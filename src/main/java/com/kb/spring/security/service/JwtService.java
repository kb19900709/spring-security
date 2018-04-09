package com.kb.spring.security.service;

import com.kb.spring.security.service.prop.JwtProp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Autowired
    private JwtProp jwtProp;

    public String getJwtStr(String name, Map<String, Object> params) {
        Date expirationTime = new Date(System.currentTimeMillis() + jwtProp.getExpirationTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        params.put(jwtProp.getExpirationString(), sdf.format(expirationTime));
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

    public String getRoleKey() {
        return jwtProp.getRoleKey();
    }

    public String getSessionKey() {
        return jwtProp.getSessionKey();
    }
}
