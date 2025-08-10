package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDateTime;

public class MessageResponseDTO {
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private String myName;

    public MessageResponseDTO(String senderName, String content, LocalDateTime sentAt, Boolean isRead, String myName) {
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
        this.isRead = isRead;
        this.myName = myName;
    }

    // getters et setters

    public String getMyName(){
        return this.myName;
    }

    public void setMyName(String myName){
        this.myName = myName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setIsRead(Boolean isRead){
        this.isRead = isRead;
    }

    public Boolean getIsRead(){
        return this.isRead;
    }
}