package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.core.utilities.results.SuccessDataResult;
import com.questApplication.questApplication.core.utilities.results.ErrorDataResult;
import com.questApplication.questApplication.core.utilities.results.SuccessResult;
import com.questApplication.questApplication.core.utilities.results.ErrorResult;
import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.dto.CommentDTO;

import com.questApplication.questApplication.mapper.CommentMapper;
import com.questApplication.questApplication.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentManager implements CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentManager.class);
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentManager(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public DataResult<List<CommentDTO>> getAllComments() {
        logger.info("Tüm yorumlar getiriliyor");
        try {
            List<Comment> comments = commentRepository.findAll();
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Toplam {} yorum başarıyla getirildi", commentDTOs.size());
            return new SuccessDataResult<>(commentDTOs, "Yorumlar başarıyla getirildi");
        } catch (Exception e) {
            logger.error("Yorumlar getirilirken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Yorumlar getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<CommentDTO> getCommentById(Long id) {
        logger.info("{} ID'li yorum getiriliyor", id);
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null) {
                CommentDTO commentDTO = commentMapper.toDTO(comment);
                logger.info("{} ID'li yorum başarıyla getirildi", id);
                return new SuccessDataResult<>(commentDTO, "Yorum başarıyla getirildi");
            } else {
                logger.warn("{} ID'li yorum bulunamadı", id);
                return new ErrorDataResult<>(null, "Yorum bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li yorum getirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Yorum getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<List<CommentDTO>> getCommentsByPostId(Long postId) {
        logger.info("{} ID'li gönderinin yorumları getiriliyor", postId);
        try {
            List<Comment> comments = commentRepository.findByPostId(postId);
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("{} ID'li gönderinin {} yorumu başarıyla getirildi", postId, commentDTOs.size());
            return new SuccessDataResult<>(commentDTOs, "Gönderi yorumları başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li gönderinin yorumları getirilirken bir hata oluştu", postId, e);
            return new ErrorDataResult<>(null, "Gönderi yorumları getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<List<CommentDTO>> getCommentsByUserId(Long userId) {
        logger.info("{} ID'li kullanıcının yorumları getiriliyor", userId);
        try {
            List<Comment> comments = commentRepository.findByUserId(userId);
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(commentMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("{} ID'li kullanıcının {} yorumu başarıyla getirildi", userId, commentDTOs.size());
            return new SuccessDataResult<>(commentDTOs, "Kullanıcı yorumları başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcının yorumları getirilirken bir hata oluştu", userId, e);
            return new ErrorDataResult<>(null, "Kullanıcı yorumları getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<CommentDTO> createComment(CommentDTO commentDTO) {
        logger.info("Yeni yorum oluşturuluyor");
        try {
            Comment comment = commentMapper.toEntity(commentDTO);
            Comment savedComment = commentRepository.save(comment);
            CommentDTO savedCommentDTO = commentMapper.toDTO(savedComment);
            logger.info("{} ID'li yeni yorum başarıyla oluşturuldu", savedCommentDTO.getId());
            return new SuccessDataResult<>(savedCommentDTO, "Yorum başarıyla oluşturuldu");
        } catch (Exception e) {
            logger.error("Yorum oluşturulurken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Yorum oluşturulurken bir hata oluştu");
        }
    }

    @Override
    public DataResult<CommentDTO> updateComment(Long id, CommentDTO commentDTO) {
        logger.info("{} ID'li yorum güncelleniyor", id);
        try {
            Comment existingComment = commentRepository.findById(id).orElse(null);
            if (existingComment != null) {
                Comment updatedComment = commentMapper.toEntity(commentDTO);
                updatedComment.setId(id);
                Comment savedComment = commentRepository.save(updatedComment);
                CommentDTO savedCommentDTO = commentMapper.toDTO(savedComment);
                logger.info("{} ID'li yorum başarıyla güncellendi", id);
                return new SuccessDataResult<>(savedCommentDTO, "Yorum başarıyla güncellendi");
            } else {
                logger.warn("{} ID'li yorum bulunamadı", id);
                return new ErrorDataResult<>(null, "Güncellenecek yorum bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li yorum güncellenirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Yorum güncellenirken bir hata oluştu");
        }
    }

    @Override
    public Result deleteComment(Long id) {
        logger.info("{} ID'li yorum siliniyor", id);
        try {
            if (commentRepository.existsById(id)) {
                commentRepository.deleteById(id);
                logger.info("{} ID'li yorum başarıyla silindi", id);
                return new SuccessResult("Yorum başarıyla silindi");
            } else {
                logger.warn("{} ID'li yorum bulunamadı", id);
                return new ErrorResult("Silinecek yorum bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li yorum silinirken bir hata oluştu", id, e);
            return new ErrorResult("Yorum silinirken bir hata oluştu");
        }
    }
}