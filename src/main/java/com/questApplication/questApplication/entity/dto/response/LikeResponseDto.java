package com.questApplication.questApplication.entity.dto.response;

public class LikeResponseDto {
    private Long userId;
    private Long postId;
    private Long commentId;

    public Long getUserId() {
        return userId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
