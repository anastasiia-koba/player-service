package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

public class JsonNodeUtil {

    /**
     * Makes JsonNode with nested Object regular flat Json
     *
     * For example:
     * JsonNode "{key1: val1; key2: [key3: val2, key4: val3]}"
     * converts to "{key1: val1; key2.key3: val2; key2.key4: val3}"
     *
     * @param objectMapper suitable ObjectMapper for creation new JsonNode
     * @param node JsonNode with nested Object
     * @return flatten JsonNode without nested Objects
     */
    public static JsonNode flatten(ObjectMapper objectMapper, JsonNode node) {
        ObjectNode flattenNode = objectMapper.createObjectNode();
        flattenObject(node, flattenNode, "");
        return flattenNode;
    }

    static void flattenObject(JsonNode node, ObjectNode parent, String prefix) {
        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode childNode = node.get(fieldName);
            if (childNode.isObject()) {
                flattenObject(childNode, parent, prefix + fieldName + ".");
            } else {
                parent.put(prefix + fieldName, childNode.asText());
            }
        }
    }
}
