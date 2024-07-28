package com.questApplication.questApplication.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String status;
    private LocalDateTime createdAt;
    private Long userId;
    private Long postId;
    private List<Long> likeIds;

}