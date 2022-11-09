package com.example.agoraproject;

public class chat_message_models {
    String username;
    String text;
    boolean me_or_not;
    public chat_message_models(String username, String text, boolean me_or_not) {
        this.username = username;
        this.text = text;
        this.me_or_not = me_or_not;
    }

    public boolean isMe_or_not() {
        return me_or_not;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }
}
