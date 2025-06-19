package com.example.userservice.validator;

import org.springframework.stereotype.Component;

@Component
public class UserValidator {
    public boolean checkIfNotEmpty(String data){
        return data != null && !data.isEmpty();
    }

    public boolean checkLength(String data, int minLength, int maxLength){
        return data.length() >= minLength && data.length() <= maxLength;
    }
}
