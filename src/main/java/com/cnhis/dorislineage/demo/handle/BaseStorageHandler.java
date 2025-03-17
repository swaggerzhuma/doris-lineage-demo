package com.cnhis.dorislineage.demo.handle;


import com.cnhis.dorislineage.demo.dto.LineageContext;

/**
 * 存储处理器
 */
public interface BaseStorageHandler {

    void handle(LineageContext context);
}
