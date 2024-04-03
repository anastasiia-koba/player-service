package org.example.controller;

import org.example.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PlayerService playerService;

    @Test
    void testGetAllPlayers() throws Exception {

        String expectedString = "test, csv";
        when(playerService.getCsvPlayersInformation()).thenReturn(expectedString);

        byte[] expectedBytes = expectedString.getBytes();
        mvc.perform(MockMvcRequestBuilders
                        .get("/player"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/csv;charset=UTF-8"))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=players-detail.csv"))
                .andExpect(result -> {
                    byte[] actualBytes = result.getResponse().getContentAsByteArray();
                    assertArrayEquals(expectedBytes, actualBytes);
                });

        verify(playerService, times(1)).getCsvPlayersInformation();
    }

    //todo add tests for different exceptions
}