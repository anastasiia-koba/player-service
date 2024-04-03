package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvServiceTest {

    private CsvService csvService = new CsvService();


    @Test
    void testConvertJsonNodeToCsvString() {
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree("[{\"id\":14,\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"position\":\"C\",\"team.id\":12,\"team.full_name\":\"Indiana Pacers\"}, " +
                    "{\"id\":16,\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"team.id\":12,\"team.full_name\":\"Indiana Pacers\",\"team.abbreviation\":\"IND\"}]");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String result = csvService.convertJsonNodeToCsvString(jsonNode);
        String expected = "id,first_name,last_name,position,team.id,team.full_name,team.abbreviation\n" +
                "14,Ike,Anigbogu,C,12,\"Indiana Pacers\",\n" +
                "16,Ike,Anigbogu,,12,\"Indiana Pacers\",IND\n";
        assertEquals(expected, result);
    }

    // todo add test for file reading
    // todo add test with exception
}