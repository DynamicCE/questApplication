package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.results.*;
import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.dto.CommentDTO;
import com.questApplication.questApplication.mapper.CommentMapper;
import com.questApplication.questApplication.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DataResult<Page<CommentDTO>> getAllComments(Pageable pageable) {
        logger.info("Tüm yorumlar getiriliyor. Sayfa: {}, Boyut: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Comment> comments = commentRepository.findAllByStatusNot("D", pageable);
            Page<CommentDTO> commentDTOs = comments.map(commentMapper::toDTO);
            logger.info("Toplam {} yorum başarıyla getirildi", commentDTOs.getTotalElements());
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
            Comment comment = commentRepository.findByIdAndStatusNot(id, "D").orElse(null);
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
    public DataResult<Page<CommentDTO>> getCommentsByPostId(Long postId, Pageable pageable) {
        logger.info("{} ID'li gönderinin yorumları getiriliyor. Sayfa: {}, Boyut: {}", postId, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Comment> comments = commentRepository.findByPostIdAndStatusNot(postId, "D", pageable);
            Page<CommentDTO> commentDTOs = comments.map(commentMapper::toDTO);
            logger.info("{} ID'li gönderinin {} yorumu başarıyla getirildi", postId, commentDTOs.getTotalElements());
            return new SuccessDataResult<>(commentDTOs, "Gönderi yorumları başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li gönderinin yorumları getirilirken bir hata oluştu", postId, e);
            return new ErrorDataResult<>(null, "Gönderi yorumları getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<CommentDTO>> getCommentsByUserId(Long userId, Pageable pageable) {
        logger.info("{} ID'li kullanıcının yorumları getiriliyor. Sayfa: {}, Boyut: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Comment> comments = commentRepository.findByUserIdAndStatusNot(userId, "D", pageable);
            Page<CommentDTO> commentDTOs = comments.map(commentMapper::toDTO);
            logger.info("{} ID'li kullanıcının {} yorumu başarıyla getirildi", userId, commentDTOs.getTotalElements());
            return new SuccessDataResult<>(commentDTOs, "Kullanıcı yorumları başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcının yorumları getirilirken bir hata oluştu", userId, e);
            return new ErrorDataResult<>(null, "Kullanıcı yorumları getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<CommentDTO> createComment(CommentDTO commentDTO) {
        logger.info("Yeni yorum oluşturuluyor");
        try {
            Comment comment = commentMapper.toEntity(commentDTO);
            comment.setStatus("A"); // Active
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
    @Transactional
    public DataResult<CommentDTO> updateComment(Long id, CommentDTO commentDTO) {
        logger.info("{} ID'li yorum güncelleniyor", id);
        try {
            Comment existingComment = commentRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (existingComment != null) {
                Comment updatedComment = commentMapper.toEntity(commentDTO);
                updatedComment.setId(id);
                updatedComment.setStatus("U"); // Updated
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
    @Transactional
    public Result deleteComment(Long id) {
        logger.info("{} ID'li yorum siliniyor (soft delete)", id);
        try {
            Comment comment = commentRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (comment != null) {
                comment.setStatus("D"); // Deleted
                commentRepository.save(comment);
                logger.info("{} ID'li yorum başarıyla silindi (soft delete)", id);
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

    @Override
    @Transactional
    public DataResult<CommentDTO> activateComment(Long id) {
        logger.info("{} ID'li yorum aktifleştiriliyor", id);
        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment != null && !comment.getStatus().equals("A")) {
                comment.setStatus("A"); // Active
                Comment savedComment = commentRepository.save(comment);
                CommentDTO activatedCommentDTO = commentMapper.toDTO(savedComment);
                logger.info("{} ID'li yorum başarıyla aktifleştirildi", id);
                return new SuccessDataResult<>(activatedCommentDTO, "Yorum başarıyla aktifleştirildi");
            } else {
                logger.warn("{} ID'li yorum bulunamadı veya zaten aktif", id);
                return new ErrorDataResult<>(null, "Aktifleştirilecek yorum bulunamadı veya zaten aktif");
            }
        } catch (Exception e) {
            logger.error("{} ID'li yorum aktifleştirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Yorum aktifleştirilirken bir hata oluştu");
        }
    }
}