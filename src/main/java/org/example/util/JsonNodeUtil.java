package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

public class JsonNodeUtil {

    /**
     * Makes JsonNode with nested Object regular flat Json, works with Arrays as well
     * For example:
     * JsonNode "{key1: val1; key2: [key3: val2, key4: val3]}"
     * converts to "{key1: val1; key2.key3: val2; key2.key4: val3}"
     *
     * @param objectMapper suitable ObjectMapper for creation new JsonNode
     * @param node JsonNode with nested Object
     * @return flatten JsonNode without nested Objects
     */
    public static JsonNode flatten(ObjectMapper objectMapper, JsonNode node) {
        JsonNode result = null;
        if (node.isObject()) {
            ObjectNode flattenNode = objectMapper.createObjectNode();
            flattenObject(node, flattenNode, "");
            result = flattenNode;
        } else if (node.isArray()) {
            ArrayNode arrayFlattenNode = objectMapper.createArrayNode();
            Iterator<JsonNode> elementIterator = node.elements();
            while (elementIterator.hasNext()) {
                ObjectNode flattenNode = objectMapper.createObjectNode();
                flattenObject(elementIterator.next(), flattenNode, "");
                arrayFlattenNode.add(flattenNode);
            }
            result = arrayFlattenNode;
        }
        return result;
    }

    private static void flattenObject(JsonNode node, ObjectNode parent, String prefix) {
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
