package com.griddynamics.storeapplication.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionManagerService {

  private final Map<String, Long> sessionStore = new ConcurrentHashMap<>();

  public String createSession(Long userId) {
    String sessionId = UUID.randomUUID().toString();
    sessionStore.put(sessionId, userId);

    return sessionId;
  }

  public Long getUserId(String sessionId) {
    return sessionStore.get(sessionId);
  }

}
