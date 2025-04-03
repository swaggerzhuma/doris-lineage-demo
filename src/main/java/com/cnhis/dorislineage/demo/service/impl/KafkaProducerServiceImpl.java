package com.cnhis.dorislineage.demo.service.impl;

import com.cnhis.dorislineage.demo.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author liuqiang
 * @date 2025/3/31 15:38
 */
@Service
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.info("发送消息到{},msg:\n{}", topic, message);
    }
}
