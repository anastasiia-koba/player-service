package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.example.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerServiceTest {
    private PlayerService playerService;

    @Mock
    private CsvService csvService;

    @Mock
    private PlayerDetailProvider playerDetailProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(csvService, playerDetailProvider, objectMapper);
    }

    @Test
    public void testGetCsvPlayersInformation() {
        // Mocking players and player details
        List<Player> players = new ArrayList<>();
        Player player1 = new Player(1, "John Doe");
        Player player2 = new Player(2, "Jane Smith");
        players.add(player1);
        players.add(player2);

        JsonNode player1Details = objectMapper.createObjectNode();
        JsonNode player2Details = objectMapper.createObjectNode();

        when(csvService.getTypesListFromCsv()).thenReturn(Arrays.asList(players.toArray()));
        when(playerDetailProvider.getPlayerDetails(1)).thenReturn(player1Details);
        when(playerDetailProvider.getPlayerDetails(2)).thenReturn(player2Details);
        when(csvService.convertJsonNodeToCsvString(any(ArrayNode.class))).thenReturn("CSV_String");

        String csvPlayersInformation = playerService.getCsvPlayersInformation();

        assertEquals("CSV_String", csvPlayersInformation);

        verify(csvService).getTypesListFromCsv();
        verify(playerDetailProvider, times(2)).getPlayerDetails(anyLong());
        verify(csvService).convertJsonNodeToCsvString(any(ArrayNode.class));
    }

    @Test
    public void testRefreshCache() {
        // Mocking players
        List<Player> players = new ArrayList<>();
        Player player1 = new Player(1, "John Doe");
        Player player2 = new Player(2, "Jane Smith");
        players.add(player1);
        players.add(player2);

        JsonNode player1Details = objectMapper.createObjectNode();
        JsonNode player2Details = objectMapper.createObjectNode();

        when(csvService.getTypesListFromCsv()).thenReturn(Arrays.asList(players.toArray()));
        when(playerDetailProvider.getPlayerDetails(1)).thenReturn(player1Details);
        when(playerDetailProvider.getPlayerDetails(2)).thenReturn(player2Details);

        playerService.refreshCache();

        verify(csvService).getTypesListFromCsv();
        verify(playerDetailProvider, times(2)).getPlayerDetails(anyLong());
    }

    // todo tests for some exceptions
}