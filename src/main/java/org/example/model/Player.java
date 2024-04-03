package org.example.model;

/**
 * Simple DTO for keeping Player's information
 * @param id Player id
 * @param nickname Player nickname
 */
public record Player(long id, String nickname) {}
