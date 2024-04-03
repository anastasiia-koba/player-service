package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.util.JsonNodeUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Service for caching responses from BalldontlieService
 */
@Service
public class CachedBalldontlieService implements PlayerDetailProvider {

    private BalldontlieService balldontlieService;
    private ObjectMapper objectMapper;

    public CachedBalldontlieService(BalldontlieService balldontlieService, ObjectMapper objectMapper) {
        this.balldontlieService = balldontlieService;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "playerCache", key = "#playerId")
    @Override
    public JsonNode getPlayerDetails(long playerId) {
        JsonNode playerDetails = balldontlieService.getPlayerDetails(playerId);
        return JsonNodeUtil.flatten(objectMapper, playerDetails);
    }
}
