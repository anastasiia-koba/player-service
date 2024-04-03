package org.example.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.example.model.Player;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Service for processing CSV files
 */
@Service
public class CsvService {

    @Value("${csv.source.file.path}")
    private Resource source;

    public <T> List<T> getTypesListFromCsv() {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper mapper = new CsvMapper();

        try (InputStreamReader inputStreamReader = new InputStreamReader(source.getInputStream())) {
            MappingIterator<T> readValues = mapper.reader(Player.class)
                    .with(bootstrapSchema)
                    .readValues(inputStreamReader);
            return readValues.readAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertJsonNodeToCsvString(JsonNode node) {
        try {
            return getObjectWriter(node).writeValueAsString(node);
        } catch (JsonProcessingException e) {
            // todo add logger
            throw new RuntimeException(e);
        }
    }

    private ObjectWriter getObjectWriter(JsonNode flattenedNode) {
        // Configure CSV mapper
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        addHeaders(flattenedNode, csvSchemaBuilder);

        // Build CSV schema
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        return csvMapper.writerFor(JsonNode.class)
                .with(csvSchema);
    }

    // Add headers to CSV schema
    private void addHeaders(JsonNode node, CsvSchema.Builder csvSchemaBuilder) {
        if (node.isArray() && node.size() > 0) {
            for (JsonNode element : node) {
                element.fieldNames().forEachRemaining(fieldName -> {
                    if (!csvSchemaBuilder.hasColumn(fieldName)) csvSchemaBuilder.addColumn(fieldName);
                });
            }
        }
    }
}
