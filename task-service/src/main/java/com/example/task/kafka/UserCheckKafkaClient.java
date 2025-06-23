package com.example.task.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class UserCheckKafkaClient {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, CompletableFuture<Boolean>> pendingChecks = new ConcurrentHashMap<>();

    public boolean checkUserExists(Long userId) throws Exception {
        String correlationId = UUID.randomUUID().toString();
        UserCheckRequest request = new UserCheckRequest();
        request.setUserId(userId);
        request.setCorrelationId(correlationId);

        ObjectMapper mapper = new ObjectMapper();
        kafkaTemplate.send("check-user-exists-request", mapper.writeValueAsString(request));
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingChecks.put(correlationId, future);
        return future.get(15, TimeUnit.SECONDS);
    }

    @KafkaListener(topics = "check-user-exists-response", groupId = "task-manager")
    public void onResponse(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserCheckResponse response = mapper.readValue(message, UserCheckResponse.class);
        CompletableFuture<Boolean> future = pendingChecks.get(response.getCorrelationId());
        if(future != null) {
            future.complete(response.isExists());
        }
    }
}
