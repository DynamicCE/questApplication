package com.questApplication.questApplication.entity.dto.request;

import jakarta.validation.constraints.NotNull;

public class CommentRequestDto {
    @NotNull(message = "Yorum metni boş olamaz")
    private String text;

    @NotNull(message = "Gönderi ID'si boş olamaz")
    private Long postId;

    public String getText() {
        return text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
