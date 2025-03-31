package com.cnhis.dorislineage.demo.service;

import java.util.List;

/**
 * @author liuqiang
 * @date 2025/2/17 16:55
 */
public interface DorisDatabaseService {

    List<String> listDatabases();

    List<String> listTables(String database);

    List<String> listFields(String database, String table);


}
