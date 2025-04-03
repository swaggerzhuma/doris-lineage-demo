package com.cnhis.dorislineage.demo.linstener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cnhis.dorislineage.demo.constants.NeoConstant;
import com.cnhis.dorislineage.demo.dto.DorisSqlAudit;
import com.cnhis.dorislineage.demo.dto.LineageContext;
import com.cnhis.dorislineage.demo.handle.BaseMessageHandler;
import com.cnhis.dorislineage.demo.handle.BaseStorageHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * doris审计日志监听器
 *
 * @author liuqiang
 * @date 2025/4/2 16:56
 */
@Component
@Slf4j
public class DorisAuditLogListener {

    @Autowired
    private Map<String, BaseMessageHandler> messageHandlerMap;

    @Autowired
    private BaseStorageHandler mergeStorageHandler;


    /**
     * 消费doris审计日志，解析数据血缘，写入neo4j
     *
     * @param record
     * @param acknowledgment
     */
    @KafkaListener(topics = "doris-audit", containerFactory = "customerContainerFactory", groupId = "flume")
    public void handleDorisAuditMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            if (record != null) {
                String stmt = record.value();
                if (stmt.contains("audit_log") || stmt.contains("insert into test.lineage_json")) {
                    return;
                }
                DorisSqlAudit audit = new DorisSqlAudit();
                audit.setStmt(stmt);
                // 获取消息处理器
                BaseMessageHandler messageHandler = messageHandlerMap.get(NeoConstant.SourceType.SQL);
                Objects.requireNonNull(messageHandler, "messageHandler required");
                // 获取消息上下文
                LineageContext lineageContext = messageHandler.handle(audit);
                log.info("datasource:\n{}", JSON.toJSONString(lineageContext.getDataSourceNodeList(), SerializerFeature.PrettyFormat));
                log.info("db：\n{}", JSON.toJSONString(lineageContext.getDbNodeList(), SerializerFeature.PrettyFormat));
                log.info("table：\n{}", JSON.toJSONString(lineageContext.getTableNodeList(), SerializerFeature.PrettyFormat));
                log.info("field：\n{}", JSON.toJSONString(lineageContext.getFieldNodeList(), SerializerFeature.PrettyFormat));
                log.info("relation：\n{}", JSON.toJSONString(lineageContext.getRelationNodeList(), SerializerFeature.PrettyFormat));
                Objects.requireNonNull(lineageContext, "lineageContext required");
                // 消息存储
                mergeStorageHandler.handle(lineageContext);
            }
        } catch (Exception e) {
            log.error("kafkaListener错误：{},偏移量是：{}", e.getMessage(), record.offset());
            e.printStackTrace();
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }


    /**
     * 消费测试数据
     *
     * @param record
     * @param acknowledgment
     */
    @KafkaListener(topics = "lineage", containerFactory = "customerContainerFactory", groupId = "test_ggg")
    public void handleTestMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            if (record != null) {
                String stmt = record.value();
                if (stmt.contains("audit_log") || stmt.contains("insert into test.lineage_json")) {
                    return;
                }
                DorisSqlAudit audit = new DorisSqlAudit();
                audit.setStmt(stmt);
                // 获取消息处理器
                BaseMessageHandler messageHandler = messageHandlerMap.get(NeoConstant.SourceType.SQL);
                Objects.requireNonNull(messageHandler, "messageHandler required");
                // 获取消息上下文
                LineageContext lineageContext = messageHandler.handle(audit);
                log.info("datasource:\n{}", JSON.toJSONString(lineageContext.getDataSourceNodeList(), SerializerFeature.PrettyFormat));
                log.info("db：\n{}", JSON.toJSONString(lineageContext.getDbNodeList(), SerializerFeature.PrettyFormat));
                log.info("table：\n{}", JSON.toJSONString(lineageContext.getTableNodeList(), SerializerFeature.PrettyFormat));
                log.info("field：\n{}", JSON.toJSONString(lineageContext.getFieldNodeList(), SerializerFeature.PrettyFormat));
                log.info("relation：\n{}", JSON.toJSONString(lineageContext.getRelationNodeList(), SerializerFeature.PrettyFormat));
                Objects.requireNonNull(lineageContext, "lineageContext required");
                // 消息存储
                mergeStorageHandler.handle(lineageContext);
            }
        } catch (Exception e) {
            log.error("kafkaListener错误：{},偏移量是：{}", e.getMessage(), record.offset());
            e.printStackTrace();
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }


    /**
     * 消费flume采集的测试数据
     *
     * @param record
     * @param acknowledgment
     */
    //    @KafkaListener(topics = "doris-audit", containerFactory = "customerContainerFactory", groupId = "flume")
    public void handleFlumeTestMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            if (record != null) {
                String value = record.value();
                log.info("flume test msg:\n{}", value);
            }
        } finally {
            acknowledgment.acknowledge();
        }
    }

}
