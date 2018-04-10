package com.kb.spring.security.service;

import com.kb.spring.security.service.bean.UserAuthProfile;
import com.kb.spring.security.service.utils.TripleDes;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthService {
    private static final Map<String, UserAuthProfile> USER_PROFILES_MAP = UserAuthProfile.getUserAuthProfile();

    public boolean isValid(String name, String password) {
        UserAuthProfile userAuthProfile = Optional.ofNullable(USER_PROFILES_MAP.get(name))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userAuthProfile.getUserName().equals(name) && userAuthProfile.getPassword().equals(TripleDes.encode(password));
    }

    public List<String> findUserAuthorities(String name) {
        UserAuthProfile userAuthProfile = USER_PROFILES_MAP.get(name);
        return Stream.concat(
                Stream.of(userAuthProfile.getRoles().split(","))
                , Stream.of(userAuthProfile.getAuths().split(","))
        ).collect(Collectors.toList());
    }
}
