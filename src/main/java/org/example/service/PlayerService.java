package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.example.model.Player;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private CsvService csvService;
    private PlayerDetailProvider playerDetailProvider;

    private ObjectMapper objectMapper;

    public PlayerService(CsvService csvService,
                         PlayerDetailProvider cachedBalldontlieService,
                         ObjectMapper objectMapper) {
        this.csvService = csvService;
        this.playerDetailProvider = cachedBalldontlieService;
        this.objectMapper = objectMapper;
    }

    public String getCsvPlayersInformation() {
        List<Player> players = csvService.getTypesListFromCsv();

        ArrayNode result = objectMapper.createArrayNode();
        for (Player player: players) {
            JsonNode playerDetails = playerDetailProvider.getPlayerDetails(player.id());
            result.add(playerDetails);
        }
        return csvService.convertJsonNodeToCsvString(result);
    }

    @Scheduled(fixedRateString = "#{${cache.update-time-in-minutes} * 60000}")
    public void refreshCache() {
        List<Player> players = csvService.getTypesListFromCsv();

        for (Player player: players) {
            playerDetailProvider.getPlayerDetails(player.id());
        }
    }
}
