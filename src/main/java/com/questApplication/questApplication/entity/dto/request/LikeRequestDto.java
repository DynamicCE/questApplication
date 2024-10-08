package com.questApplication.questApplication.entity.dto.request;

import jakarta.validation.constraints.AssertTrue;

public class LikeRequestDto {

    private Long postId;
    private Long commentId;

    @AssertTrue(message = "postId veya commentId'den en az biri dolu olmalı")
    public boolean isValid() {
        return postId != null || commentId != null;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
