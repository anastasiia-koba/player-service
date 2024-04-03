package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JsonNodeUtilTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFlatten() {
        JsonNode jsonNode = null;
        JsonNode expected = null;
        try {
            jsonNode = objectMapper.readTree("{\"id\":14,\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"position\":\"C\",\"team\":{\"id\":12,\"full_name\":\"Indiana Pacers\"}}");
            expected = objectMapper.readTree("{\"id\":\"14\",\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"position\":\"C\",\"team.id\":\"12\",\"team.full_name\":\"Indiana Pacers\"}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode result = JsonNodeUtil.flatten(objectMapper, jsonNode);

        assertEquals(expected, result);
    }

    //todo add tests with different JsonNode
}