package com.cnhis.dorislineage.demo.service;

import java.util.List;

/**
 * @author liuqiang
 * @date 2025/2/17 16:55
 */
public interface DatabaseService {

    List<String> getDatabases();

    List<String> getTables(String database);

    List<String> getColumns(String database, String table);


}
