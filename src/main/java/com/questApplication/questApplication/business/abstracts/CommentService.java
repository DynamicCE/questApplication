package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<CommentResponseDto> getCommentsByPostId(Long postId, Pageable pageable);

    Page<CommentResponseDto> getCommentsByCurrentUser(String username, Pageable pageable);

    CommentResponseDto createComment(CommentRequestDto commentRequestDto, String username);

    CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, String username);

    void deleteComment(Long id, String username);

    CommentResponseDto getCommentById(Long id);
}

