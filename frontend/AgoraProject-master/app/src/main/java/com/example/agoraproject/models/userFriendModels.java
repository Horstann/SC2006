package com.example.agoraproject.models;

public class userFriendModels {
    String username;
    String useremail;

    public userFriendModels(String username, String useremail) {
        this.username = username;
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public String getUseremail() {
        return useremail;
    }
}
