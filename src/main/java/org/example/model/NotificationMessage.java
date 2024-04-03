package org.example.model;

import java.time.LocalDateTime;

/**
 * Message to WebSocket notification topic
 * @param time Time of update
 * @param text Updated information
 */
public record NotificationMessage(LocalDateTime time, String text) { }