package com.questApplication.questApplication.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String status;
    private String text;
    private LocalDateTime createdAt;
    private UserDTO user;
    private List<LikeDTO> likes;
    private List<CommentDTO> comments;
}