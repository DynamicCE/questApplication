package com.questApplication.questApplication.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String status;
    private Long userId;
    private Long postId;
    private List<Long> likeIds;
}