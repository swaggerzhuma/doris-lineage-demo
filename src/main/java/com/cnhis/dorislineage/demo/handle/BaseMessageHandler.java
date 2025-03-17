package com.cnhis.dorislineage.demo.handle;


import com.cnhis.dorislineage.demo.dto.DorisSqlAudit;
import com.cnhis.dorislineage.demo.dto.LineageContext;

/**
 * 事件处理器
 */
public interface BaseMessageHandler {

    LineageContext handle(DorisSqlAudit audit);
}
