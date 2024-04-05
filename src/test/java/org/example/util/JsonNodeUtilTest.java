package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JsonNodeUtilTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFlatten() {
        JsonNode jsonNode;
        JsonNode expected;
        try {
            jsonNode = objectMapper.readTree("{\"id\":14,\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"position\":\"C\",\"team\":{\"id\":12,\"full_name\":\"Indiana Pacers\"}}");
            expected = objectMapper.readTree("{\"id\":\"14\",\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"position\":\"C\",\"team.id\":\"12\",\"team.full_name\":\"Indiana Pacers\"}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode result = JsonNodeUtil.flatten(objectMapper, jsonNode);

        assertEquals(expected, result);
    }

    @Test
    void testFlattenIfAlreadyFlattenDoNothing() {
        JsonNode expected;
        try {
            expected = objectMapper.readTree("{\"id\":\"14\",\"first_name\":\"Ike\",\"last_name\":\"Anigbogu\",\"position\":\"C\",\"team.id\":\"12\",\"team.full_name\":\"Indiana Pacers\"}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode result = JsonNodeUtil.flatten(objectMapper, expected);

        assertEquals(expected, result);
    }

    @Test
    void testFlattenIfEmptyNode() {
        JsonNode expected = objectMapper.createObjectNode();
        JsonNode result = JsonNodeUtil.flatten(objectMapper, expected);
        assertEquals(expected, result);
    }

    @Test
    void testFlattenArray() {
        JsonNode jsonNode;
        JsonNode expected;
        try {
            jsonNode = objectMapper.readTree("[{\"id\":14,\"first_name\":\"Ike\",\"team\":{\"id\":12,\"full_name\":\"Indiana Pacers\"}},{\"id\":14,\"first_name\":\"Ike\",\"team\":{\"id\":12,\"full_name\":\"Indiana Pacers\"}}]");
            expected = objectMapper.readTree("[{\"id\":\"14\",\"first_name\":\"Ike\",\"team.id\":\"12\",\"team.full_name\":\"Indiana Pacers\"},{\"id\":\"14\",\"first_name\":\"Ike\",\"team.id\":\"12\",\"team.full_name\":\"Indiana Pacers\"}]");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode result = JsonNodeUtil.flatten(objectMapper, jsonNode);

        assertEquals(expected, result);
    }

    @Test
    void testFlattenComplexObject() {
        JsonNode jsonNode;
        JsonNode expected;
        try {
            jsonNode = objectMapper.readTree("{\"id\":14,\"first_name\":\"Ike\",\"team\":{\"id\":12,\"arr\":{\"field\":\"more\"}}}");
            expected = objectMapper.readTree("{\"id\":\"14\",\"first_name\":\"Ike\",\"team.id\":\"12\",\"team.arr.field\":\"more\"}");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode result = JsonNodeUtil.flatten(objectMapper, jsonNode);

        assertEquals(expected, result);
    }
}