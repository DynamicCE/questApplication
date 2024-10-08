package com.questApplication.questApplication.entity.dto.response;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private UserResponseDto user;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}
