package com.questApplication.questApplication.entity.dto;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String status;
    private Long userId;
    private int commentCount;
    private int likeCount;
}