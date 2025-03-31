package com.cnhis.dorislineage.demo.controller;

import com.cnhis.dorislineage.demo.service.DorisDatabaseService;
import com.cnhis.dorislineage.demo.service.DorisLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/doris-lineage")
public class DorisLineageController {

    @Autowired
    private DorisDatabaseService dorisDatabaseService;

    @Autowired
    private DorisLineageService dorisLineageService;

    /**
     * 列举所有数据库接口
     *
     * @return List<String>
     */
    @GetMapping("/list")
    public List<String> getDatabases() {
        return dorisDatabaseService.listDatabases();
    }

    /**
     * 列举数据库下所有表
     *
     * @param dbName 数据库名
     * @return List<String>
     */
    @GetMapping("/list/{dbName}")
    public List<String> getTables(@PathVariable String dbName) {
        return dorisDatabaseService.listTables(dbName);
    }

    /**
     * 列举表里所有字段
     *
     * @param dbName 数据库名
     * @param tableName    表名
     * @return List<String>
     */
    @GetMapping("/list/{dbName}/{tableName}")
    public List<String> getColumns(@PathVariable String dbName, @PathVariable String tableName) {
        return dorisDatabaseService.listFields(dbName, tableName);
    }

    /**
     * 获取字段的完整数据血缘
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param fieldName 字段名
     * @return ResponseEntity<List < Map < String, Object>>>
     */
    @GetMapping("/full-field-lineage/{dbName}/{tableName}/{fieldName}")
    public ResponseEntity<List<Map<String, Object>>> getFullFieldLineage(@PathVariable String dbName, @PathVariable String tableName, @PathVariable String fieldName) {
        return ResponseEntity.ok(dorisLineageService.getFullLineageByField(dbName, tableName, fieldName));
    }


    /**
     * 获取表的完整数据血缘
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @return ResponseEntity<List < Map < String, Object>>>
     */
    @GetMapping("/full-table-lineage/{dbName}/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> getFullTableLineage(@PathVariable String dbName, @PathVariable String tableName) {
        return ResponseEntity.ok(dorisLineageService.getFullLineageByTable(dbName, tableName));
    }

    /**
     * 获取表的一层血缘（上游和下游）
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @return ResponseEntity<List < Map < String, Object>>>
     */
    @GetMapping("/table-lineage/{dbName}/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> getTableLineage(@PathVariable String dbName, @PathVariable String tableName) {
        return ResponseEntity.ok(dorisLineageService.getLineageByTable(dbName, tableName));
    }

    /**
     * 获取字段的一层血缘（上游和下游）
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param fieldName 字段名
     * @return ResponseEntity<List < Map < String, Object>>>
     */
    @GetMapping("/field-lineage/{dbName}/{tableName}/{fieldName}")
    public ResponseEntity<List<Map<String, Object>>> getFieldLineage(@PathVariable String dbName, @PathVariable String tableName, @PathVariable String fieldName) {
        return ResponseEntity.ok(dorisLineageService.getLineageByField(dbName, tableName, fieldName));
    }
}