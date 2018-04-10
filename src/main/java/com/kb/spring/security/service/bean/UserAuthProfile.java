package com.kb.spring.security.service.bean;

import com.kb.spring.security.service.utils.TripleDes;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserAuthProfile {
    private String userName;
    private String password;
    private String roles;
    private String auths;

    // others fields
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getAuths() {
        return auths;
    }

    public void setAuths(String auths) {
        this.auths = auths;
    }

    private UserAuthProfile(String userName, String password, String roles, String auths) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.auths = auths;
    }

    public static Map<String, UserAuthProfile> getUserAuthProfile() {
        return Stream.of(
                new UserAuthProfile("boss", "i_m_boss", "BOSS,ADMIN", "R,W,D")
                , new UserAuthProfile("admin", "123456", "ADMIN", "R,W,D")
                , new UserAuthProfile("kb", "654321", "MEMBER", "R,W")
                , new UserAuthProfile("guest", "guest", "GUEST", "R"))
                .map(UserAuthProfile::encodePassword)
                .map(UserAuthProfile::transferAuthorities)
                .collect(
                        Collectors.toMap(UserAuthProfile::getUserName, userAuthProfile -> userAuthProfile)
                );
    }

    private static UserAuthProfile encodePassword(UserAuthProfile userAuthProfile) {
        String encodePassword = TripleDes.encode(userAuthProfile.getPassword());
        userAuthProfile.setPassword(encodePassword);
        return userAuthProfile;
    }

    private static UserAuthProfile transferAuthorities(UserAuthProfile userAuthProfile) {
        userAuthProfile.setRoles(
                transferForSecurity(userAuthProfile.getRoles(), "ROLE")
        );
        userAuthProfile.setAuths(
                transferForSecurity(userAuthProfile.getAuths(), "AUTH")
        );
        return userAuthProfile;
    }

    private static String transferForSecurity(String target, String prefix) {
        return Stream.of(target.split(",")).map(var ->
                String.format("%s_%s", prefix, var)
        ).collect(Collectors.joining(","));
    }
}
