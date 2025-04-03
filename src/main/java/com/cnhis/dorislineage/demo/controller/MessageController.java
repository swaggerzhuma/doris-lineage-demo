package com.cnhis.dorislineage.demo.controller;

import com.cnhis.dorislineage.demo.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuqiang
 * @date 2025/3/31 15:35
 */
@RestController
@RequestMapping("/kafka")
public class MessageController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody MessageRequest request) {
        if (request.getMessage() == null || request.getTopic() == null) {
            return ResponseEntity.badRequest().body("message和topic是必填字段");
        }
        kafkaProducerService.sendMessage(request.getTopic(), request.getMessage());
        return ResponseEntity.ok("消息已发送");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("发送消息失败: " + e.getMessage());
    }

    public static class MessageRequest {
        private String message;
        private String topic;

        // Getters and Setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}
