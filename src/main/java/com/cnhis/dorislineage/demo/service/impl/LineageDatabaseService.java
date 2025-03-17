package com.cnhis.dorislineage.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineageDatabaseService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDatabases() {
        return jdbcTemplate.queryForList("SELECT name FROM databases", String.class);
    }

    public List<String> getTables(String databaseName) {
        return jdbcTemplate.queryForList("SELECT name FROM tables WHERE database_id = (SELECT id FROM databases WHERE name = ?)", String.class, databaseName);
    }

    public List<String> getFields(String databaseName, String tableName) {
        return jdbcTemplate.queryForList("SELECT name FROM fields WHERE table_id = (SELECT id FROM tables WHERE database_id = (SELECT id FROM databases WHERE name = ?) AND name = ?)", String.class, databaseName, tableName);
    }
}