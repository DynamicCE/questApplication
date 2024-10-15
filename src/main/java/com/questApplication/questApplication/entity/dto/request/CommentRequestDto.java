package com.questApplication.questApplication.entity.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotNull
    private String text;

    private Long postId;

    private Long parentCommentId;

    @AssertTrue(message = "Post ID veya Parent Comment ID'den en az biri dolu olmalıdır")
    public boolean isValid(Long postId,Long commentId){
        return postId != null || commentId != null;
    }

}
