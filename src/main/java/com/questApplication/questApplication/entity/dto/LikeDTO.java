package com.questApplication.questApplication.entity.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private String status;
    private Long userId;
    private Long postId;
    private Long commentId;
}