package com.cnhis.dorislineage.demo.controller;

import com.cnhis.dorislineage.demo.service.DatabaseService;
import com.cnhis.dorislineage.demo.service.LineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author liuqiang
 */
@RestController
@RequestMapping("/api/db")
public class DorisLineageController {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private LineageService lineageService;

    @GetMapping("/databases")
    public List<String> getDatabases() {
        return databaseService.getDatabases();
    }

    @GetMapping("/databases/{database}/tables")
    public List<String> getTables(@PathVariable String database) {
        return databaseService.getTables(database);
    }

    @GetMapping("/databases/{database}/tables/{table}/columns")
    public List<String> getColumns(@PathVariable String database, @PathVariable String table) {
        return databaseService.getColumns(database, table);
    }

    @GetMapping("/databases/{database}/tables/{table}/lineage")
    public List<Map<String, Object>> getTableLineage(@PathVariable String database, @PathVariable String table) {
        return lineageService.getLineageByTable(database, table);
    }

    @GetMapping("/databases/{database}/tables/{table}/columns/{column}/lineage")
    public List<Map<String, Object>> getFieldLineage(@PathVariable String database, @PathVariable String table, @PathVariable String column) {
        return lineageService.getLineageByField(database, table, column);
    }
}