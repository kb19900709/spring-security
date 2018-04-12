package com.kb.spring.security.controller;

import com.kb.spring.security.controller.bean.SessionMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController extends BaseController {

    private static final String BROWSER = "browser";

    @GetMapping("test")
    @PreAuthorize("hasAnyRole('BOSS','ADMIN') or hasAuthority('AUTH_W')")
    public SessionMessage cookie(@RequestParam("browser") String browser) {
        SessionMessage result = new SessionMessage();

        String sessionBrowser = getCache(BROWSER, String.class);
        if (sessionBrowser == null) {
            result.setSessionMsg(String.format("session has no attributes，set browser: %s, sessionId: %s", browser, getHttpSession().getId()));
            setCache(BROWSER, browser);
        } else {
            result.setSessionMsg(String.format("session has attributes，browser: %s, sessionId: %s", sessionBrowser, getHttpSession().getId()));
        }

        return result;
    }
}
