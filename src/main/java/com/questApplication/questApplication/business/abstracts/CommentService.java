package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    DataResult<Page<CommentDTO>> getCommentsByPostId(Long postId, Pageable pageable);
    DataResult<Page<CommentDTO>> getCommentsByUserId(Long userId, Pageable pageable);
    DataResult<CommentDTO> createComment(CommentDTO commentDTO);
    DataResult<CommentDTO> updateComment(Long id, CommentDTO commentDTO);
    Result deleteComment(Long id);
    DataResult<CommentDTO> getCommentById(Long id);
}
