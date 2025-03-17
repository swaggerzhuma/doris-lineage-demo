package com.cnhis.dorislineage.demo.service.impl;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.neo4j.driver.Values.parameters;

@Service
public class LineageServiceImpl  {
    private final Driver driver;

    public LineageServiceImpl(Driver driver) {
        this.driver = driver;
    }

    public void saveLineageData(List<Map<String, Object>> lineageJson) {
        Session session = driver.session();

        for (Map<String, Object> entry : lineageJson) {
            List<Map<String, String>> sourceFields = (List<Map<String, String>>) entry.get("sourceFields");
            Map<String, String> targetField = (Map<String, String>) entry.get("targetField");

            // Create nodes and relationships based on the JSON structure
            createNodesAndRelationships(session, sourceFields, targetField);
        }

        session.close();
    }

    private void createNodesAndRelationships(Session session, List<Map<String, String>> sourceFields, Map<String, String> targetField) {
        // Assuming you have a method to generate unique IDs or use existing ones
        String sourceNodeId = generateUniqueNodeId(sourceFields);
        String targetNodeId = generateUniqueNodeId(List.of(targetField));

        // Example Cypher queries to create nodes and relationships
        String createSourceNodeQuery = "MERGE (s:Field {id: $sourceId}) SET s += $props";
        String createTargetNodeQuery = "MERGE (t:Field {id: $targetId}) SET t += $props";
        String createRelationQuery = "MATCH (s:Field {id: $sourceId}), (t:Field {id: $targetId}) MERGE (s)-[:MAPS_TO]->(t)";

        // Execute Cypher queries
        session.run(createSourceNodeQuery, parameters("sourceId", sourceNodeId, "props", sourceFields));
        session.run(createTargetNodeQuery, parameters("targetId", targetNodeId, "props", targetField));
        session.run(createRelationQuery, parameters("sourceId", sourceNodeId, "targetId", targetNodeId));
    }

    private String generateUniqueNodeId(List<Map<String, String>> fields) {
        // Implement logic to generate unique node ID from field details
        return fields.stream()
                .map(field -> String.format("%s.%s.%s", field.get("dbName"), field.get("tableName"), field.get("fieldName")))
                .collect(Collectors.joining("_"));
    }
}