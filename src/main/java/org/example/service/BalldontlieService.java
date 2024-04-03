package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for interaction with Balldontlie API
 */
@Service
public class BalldontlieService implements PlayerDetailProvider {
    private RestTemplate ballDontLieRestTemplate;
    Logger logger = LoggerFactory.getLogger(BalldontlieService.class);

    public BalldontlieService(RestTemplate ballDontLieRestTemplate) {
        this.ballDontLieRestTemplate = ballDontLieRestTemplate;
    }

    @Override
    public JsonNode getPlayerDetails(long playerId) {
        // todo exception handler
        ResponseEntity<JsonNode> response =
                ballDontLieRestTemplate.exchange("/players/{playerId}", HttpMethod.GET, null, JsonNode.class, playerId);

        JsonNode data = response.getBody();
        var fieldName = "data";
        if (data != null && data.has(fieldName)) {
            return data.get(fieldName);
        } else {
            logger.warn("Empty response for playerId=" + playerId);
            return null;
        }
    }
}
