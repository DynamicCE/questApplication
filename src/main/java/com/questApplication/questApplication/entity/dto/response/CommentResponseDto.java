package com.questApplication.questApplication.entity.dto.response;

import java.time.LocalDateTime;

public class CommentResponseDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private UserResponseDto user;
    private Long postId;

    public Long getId() {
        return id;
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

    public Long getPostId() {
        return postId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}