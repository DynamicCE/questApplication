package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.dto.CommentDTO;
import com.questApplication.questApplication.mapper.CommentMapper;
import com.questApplication.questApplication.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentManager implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentManager(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public DataResult<Page<CommentDTO>> getCommentsByPostId(Long postId, Pageable pageable) {
        try {
            Page<Comment> comments = commentRepository.findByPostIdAndStatusNot(postId, "D", pageable);
            Page<CommentDTO> commentDTOs = comments.map(commentMapper::toDTO);
            return new SuccessDataResult<>(commentDTOs, "Yorumlar başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Yorumlar getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<CommentDTO>> getCommentsByUserId(Long userId, Pageable pageable) {
        try {
            Page<Comment> comments = commentRepository.findByUserIdAndStatusNot(userId, "D", pageable);
            Page<CommentDTO> commentDTOs = comments.map(commentMapper::toDTO);
            return new SuccessDataResult<>(commentDTOs, "Kullanıcının yorumları başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcının yorumları getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<CommentDTO> createComment(CommentDTO commentDTO) {
        try {
            Comment comment = commentMapper.toEntity(commentDTO);
            comment.setStatus("A"); // Active
            Comment savedComment = commentRepository.save(comment);
            CommentDTO savedCommentDTO = commentMapper.toDTO(savedComment);
            return new SuccessDataResult<>(savedCommentDTO, "Yorum başarıyla oluşturuldu");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Yorum oluşturulurken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<CommentDTO> updateComment(Long id, CommentDTO commentDTO) {
        try {
            Comment existingComment = commentRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (existingComment != null) {
                Comment updatedComment = commentMapper.toEntity(commentDTO);
                updatedComment.setId(id);
                updatedComment.setStatus("U"); // Updated
                Comment savedComment = commentRepository.save(updatedComment);
                CommentDTO savedCommentDTO = commentMapper.toDTO(savedComment);
                return new SuccessDataResult<>(savedCommentDTO, "Yorum başarıyla güncellendi");
            } else {
                return new ErrorDataResult<>(null, "Güncellenecek yorum bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Yorum güncellenirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public Result deleteComment(Long id) {
        try {
            Comment comment = commentRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (comment != null) {
                comment.setStatus("D"); // Deleted
                commentRepository.save(comment);
                return new SuccessResult("Yorum başarıyla silindi");
            } else {
                return new ErrorResult("Silinecek yorum bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorResult("Yorum silinirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<CommentDTO> getCommentById(Long id) {
        try {
            Comment comment = commentRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (comment != null) {
                CommentDTO commentDTO = commentMapper.toDTO(comment);
                return new SuccessDataResult<>(commentDTO, "Yorum başarıyla getirildi");
            } else {
                return new ErrorDataResult<>(null, "Yorum bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Yorum getirilirken bir hata oluştu");
        }
    }
}
