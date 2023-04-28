package com.example.kiva;

import java.util.Date;

public class Message {
    private String content;
    private Date timestamp;
    private String userId;


    public Message(String content, Date timestamp, String userId) {
        this.content = content;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                '}';
    }
}


