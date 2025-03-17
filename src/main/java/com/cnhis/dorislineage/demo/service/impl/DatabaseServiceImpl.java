package com.cnhis.dorislineage.demo.service.impl;

import com.cnhis.dorislineage.demo.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getDatabases() {
        log.info("查看所有数据库");
        return jdbcTemplate.queryForList("SHOW DATABASES", String.class);
    }

    public List<String> getTables(String database) {
        log.info("查看{}的表", database);
        return jdbcTemplate.queryForList("SHOW TABLES FROM " + database, String.class);
    }

    public List<String> getColumns(String database, String table) {
        log.info("查看{}.{}的字段", database, table);
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
        return jdbcTemplate.queryForList(sql, String.class, database, table);
    }

}