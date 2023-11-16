package com.springboot.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    int status;
    Date timestamp;
    String message;
    List<String> messages;

    public int getStatus() {
        return status;
    }

    public ErrorResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ErrorResponse setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<String> getMessages() {
        return messages;
    }

    public ErrorResponse setMessages(List<String> messages) {
        this.messages = messages;
        return this;
    }
}
