package com.questApplication.questApplication.entity.dto.response;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private UserResponseDto user;
    private int likeCount;
    private boolean likedByCurrentUser;

    public
    Long getId () {
        return id;
    }

    public
    void setId ( Long id ) {
        this.id = id;
    }

    public
    String getTitle () {
        return title;
    }

    public
    void setTitle ( String title ) {
        this.title = title;
    }

    public
    String getText () {
        return text;
    }

    public
    void setText ( String text ) {
        this.text = text;
    }

    public
    LocalDateTime getCreatedAt () {
        return createdAt;
    }

    public
    void setCreatedAt ( LocalDateTime createdAt ) {
        this.createdAt = createdAt;
    }

    public
    UserResponseDto getUser () {
        return user;
    }

    public
    void setUser ( UserResponseDto user ) {
        this.user = user;
    }

    public
    int getLikeCount () {
        return likeCount;
    }

    public
    void setLikeCount ( int likeCount ) {
        this.likeCount = likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
