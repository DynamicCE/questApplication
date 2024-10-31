package com.questApplication.questApplication.service.abstracts;

import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public interface CommentService {

    Page<CommentResponseDto> getCommentsByPostId(Long postId, Pageable pageable);

    Page<CommentResponseDto> getCommentsByCommentId ( Long id, Pageable pageable );

    Page<CommentResponseDto> getCommentsByCurrentUser(String username, Pageable pageable);

    void createComment(CommentRequestDto commentRequestDto, String username);

    void updateComment(Long id, CommentRequestDto commentRequestDto, String username);

    void deleteComment(Long id, String username);

}

