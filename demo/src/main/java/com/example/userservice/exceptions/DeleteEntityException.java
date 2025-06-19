package com.example.userservice.exceptions;

public class DeleteEntityException extends RuntimeException {
    public DeleteEntityException(String message) {
        super(message);
    }
}