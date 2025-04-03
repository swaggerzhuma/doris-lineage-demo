package com.cnhis.dorislineage.demo.service;

/**
 * @author liuqiang
 * @date 2025/3/31 15:36
 */
public interface KafkaProducerService {
    void sendMessage(String topic, String message);

}
