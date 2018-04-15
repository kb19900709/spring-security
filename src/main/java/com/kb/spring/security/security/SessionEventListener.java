package com.kb.spring.security.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionEventListener.class);

    @Autowired
    private SpringSessionBackedSessionRegistry sessionRegistry;

    @EventListener
    public void onCreate(SessionCreatedEvent event) {
        String sessionId = event.getSessionId();
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        LOGGER.info(String.format("Session is created, id: %s, currentUser:%s "
                , sessionInformation.getSessionId(), sessionInformation.getPrincipal()));
        // do something
    }

    @EventListener
    public void onDestroy(SessionDestroyedEvent event) {
        LOGGER.info(String.format("Session is destroyed, id: %s", event.getSessionId()));
        // do something
    }
}
