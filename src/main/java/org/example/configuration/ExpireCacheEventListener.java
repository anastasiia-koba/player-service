package org.example.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import org.ehcache.event.EventType;
import org.example.model.NotificationMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements sending notification to WebSocket when new Value in Cache
 */
@Component
public class ExpireCacheEventListener implements CacheEventListener<Long, JsonNode> {

    private SimpMessagingTemplate simpMessagingTemplate;
    private Map<Long, JsonNode> simpleCache = new HashMap<>();

    public ExpireCacheEventListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onEvent(CacheEvent<? extends Long, ? extends JsonNode> event) {
        if (event.getType() == EventType.EXPIRED) {
            simpleCache.put(event.getKey(), event.getOldValue());
        }
        if (event.getType() == EventType.CREATED || event.getType() == EventType.UPDATED) {
            // send notification update only when have new value
            if (!simpleCache.containsKey(event.getKey()) ||
                    !simpleCache.get(event.getKey()).equals(event.getNewValue())) {
                simpMessagingTemplate.convertAndSend("/topic/pushmessages",
                        new NotificationMessage(LocalDateTime.now(), event.getNewValue().toPrettyString()));
            }
        }
    }
}

