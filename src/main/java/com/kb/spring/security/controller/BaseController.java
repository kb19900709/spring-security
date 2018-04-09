package com.kb.spring.security.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.function.Supplier;

public abstract class BaseController {

    @Autowired
    protected HttpServletRequest httpRequest;

    protected <T> void setCache(String key, Supplier<T> supplier) {
        getHttpSession().setAttribute(key, supplier.get());
    }

    protected <T> void setCache(String key, T object) {
        setCache(key, () -> object);
    }

    protected <T> T getCache(String key, Class<T> cacheClass) {
        return (T) getHttpSession().getAttribute(key);
    }

    protected HttpSession getHttpSession() {
        return httpRequest.getSession(false);
    }
}
