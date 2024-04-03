package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface PlayerDetailProvider {
    JsonNode getPlayerDetails(long playerId);
}
