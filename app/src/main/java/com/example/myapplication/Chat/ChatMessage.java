package com.example.myapplication.Chat;

public class ChatMessage {
    private String message;
    private boolean sentByMe;

    public ChatMessage(String message, boolean sentByMe) {
        this.message = message;
        this.sentByMe = sentByMe;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }
}
