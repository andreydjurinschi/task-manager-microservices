package com.example.userservice.messages;

import com.example.userservice.dao.UserDAOImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaListener {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserDAOImpl userDAO;

    @org.springframework.kafka.annotation.KafkaListener(topics = "check-user-exists-request", groupId = "user-service")
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserCheckRequest userCheckRequest = mapper.readValue(message, UserCheckRequest.class);

        boolean exists = userDAO.existsById(userCheckRequest.getUserId());

        UserCheckResponse userCheckResponse = new UserCheckResponse();
        userCheckResponse.setUserId(userCheckRequest.getUserId());
        userCheckResponse.setExists(exists);
        userCheckResponse.setCorrelationId(userCheckRequest.getCorrelationId());
        kafkaTemplate.send("check-user-exists-response", mapper.writeValueAsString(userCheckResponse));
    }

}
