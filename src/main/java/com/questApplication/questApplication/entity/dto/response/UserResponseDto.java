package com.questApplication.questApplication.entity.dto.response;

public class UserResponseDto {
    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
