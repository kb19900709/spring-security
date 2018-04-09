package com.kb.spring.security.service;

import com.kb.spring.security.service.prop.JwtProp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JwtService {

    @Autowired
    private JwtProp jwtProp;

    public String getJwtStr(String name, String roles) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Date expirationTime = new Date(System.currentTimeMillis() + jwtProp.getExpirationTime());
        return Jwts.builder()
                .claim(jwtProp.getRoleKey(), roles)
                .claim(jwtProp.getExpirationString(), sdf.format(expirationTime))
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

    public String getROLE_KEY() {
        return jwtProp.getRoleKey();
    }
}
