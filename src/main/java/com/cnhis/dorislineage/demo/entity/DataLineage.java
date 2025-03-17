package com.cnhis.dorislineage.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataLineage {
    private String sourceEntity;
    private String targetEntity;
    private String relationshipType;
    private String query;

    // Constructors, getters, setters, toString()
}