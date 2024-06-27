package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.entity.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    DataResult<List<CommentDTO>> getAllComments();
    DataResult<CommentDTO> getCommentById(Long id);
    DataResult<List<CommentDTO>> getCommentsByPostId(Long postId);
    DataResult<List<CommentDTO>> getCommentsByUserId(Long userId);
    DataResult<CommentDTO> createComment(CommentDTO commentDTO);
    DataResult<CommentDTO> updateComment(Long id, CommentDTO commentDTO);
    Result deleteComment(Long id);
}