package com.questApplication.questApplication.entity.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String status;
    private Long userId;
    private Long postId;
    private int likeCount;
}