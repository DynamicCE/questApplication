package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<CommentResponseDto> getCommentsByPostId(Long postId, Pageable pageable);

    Page<CommentResponseDto> getCommentsByCurrentUser(String username, Pageable pageable);

    CommentResponseDto getCommentById(Long id);

    void createComment(CommentRequestDto commentRequestDto, String username);

    void updateComment(Long id, CommentRequestDto commentRequestDto, String username);

    void deleteComment(Long id, String username);


}

