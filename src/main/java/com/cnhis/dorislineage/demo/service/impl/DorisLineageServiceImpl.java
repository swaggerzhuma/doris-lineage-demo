package com.cnhis.dorislineage.demo.service.impl;

import com.cnhis.dorislineage.demo.neo4j.dao.FieldRepository;
import com.cnhis.dorislineage.demo.neo4j.dao.TableRepository;
import com.cnhis.dorislineage.demo.service.DorisLineageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DorisLineageServiceImpl implements DorisLineageService {

    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private FieldRepository fieldRepository;


    @Override
    public List<Map<String, Object>> getLineageByTable(String dbName, String tableName) {
        return tableRepository.getTableBloodLine(dbName, tableName);
    }


    @Override
    public List<Map<String, Object>> getFullLineageByTable(String dbName, String tableName) {
        return tableRepository.getFullTableBloodLine(dbName, tableName);
    }

    @Override
    public List<Map<String, Object>> getFullLineageByTable2(String dbName, String tableName) {
        return tableRepository.getFullTableBloodLine2(dbName, tableName);
    }


    @Override
    public List<Map<String, Object>> getLineageByField(String dbName, String tableName, String fieldName) {
        return fieldRepository.getFieldBloodLine(dbName, tableName, fieldName);
    }


    @Override
    public List<Map<String, Object>> getFullLineageByField(String dbName, String tableName, String fieldName) {
        return fieldRepository.getFullFieldBloodLine(dbName, tableName, fieldName);
    }


    @Override
    public List<Map<String, Object>> getFullLineageByField2(String dbName, String tableName, String fieldName) {
        return fieldRepository.getFullFieldBloodLine2(dbName, tableName, fieldName);
    }
}