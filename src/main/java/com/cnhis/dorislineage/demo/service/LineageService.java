package com.cnhis.dorislineage.demo.service;

import java.util.List;
import java.util.Map;

/**
 * @author liuqiang
 * @date 2025/2/17 18:26
 */
public interface LineageService {
    List<Map<String, Object>> getLineageByField(String dbName, String tableName, String fieldName);
    List<Map<String, Object>> getLineageByTable(String dbName, String tableName);
}
