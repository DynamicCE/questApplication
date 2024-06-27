package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.results.*;
import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import com.questApplication.questApplication.mapper.LikeMapper;
import com.questApplication.questApplication.repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeManager implements LikeService {
    private static final Logger logger = LoggerFactory.getLogger(LikeManager.class);
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Autowired
    public LikeManager( LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public DataResult<Page<LikeDTO>> getAllLikes(Pageable pageable) {
        logger.info("Tüm beğeniler getiriliyor. Sayfa: {}, Boyut: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Like> likes = likeRepository.findAllByStatusTrue(pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            logger.info("Toplam {} beğeni başarıyla getirildi", likeDTOs.getTotalElements());
            return new SuccessDataResult<>(likeDTOs, "Beğeniler başarıyla getirildi");
        } catch (Exception e) {
            logger.error("Beğeniler getirilirken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Beğeniler getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<LikeDTO> getLikeById(Long id) {
        logger.info("{} ID'li beğeni getiriliyor", id);
        try {
            Like like = likeRepository.findByIdAndStatusTrue(id).orElse(null);
            if (like != null) {
                LikeDTO likeDTO = likeMapper.toDTO(like);
                logger.info("{} ID'li beğeni başarıyla getirildi", id);
                return new SuccessDataResult<>(likeDTO, "Beğeni başarıyla getirildi");
            } else {
                logger.warn("{} ID'li beğeni bulunamadı", id);
                return new ErrorDataResult<>(null, "Beğeni bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li beğeni getirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Beğeni getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<LikeDTO>> getLikesByPostId(Long postId, Pageable pageable) {
        logger.info("{} ID'li gönderinin beğenileri getiriliyor. Sayfa: {}, Boyut: {}", postId, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Like> likes = likeRepository.findByPostIdAndStatusTrue(postId, pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            logger.info("{} ID'li gönderinin {} beğenisi başarıyla getirildi", postId, likeDTOs.getTotalElements());
            return new SuccessDataResult<>(likeDTOs, "Gönderi beğenileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li gönderinin beğenileri getirilirken bir hata oluştu", postId, e);
            return new ErrorDataResult<>(null, "Gönderi beğenileri getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<LikeDTO>> getLikesByUserId(Long userId, Pageable pageable) {
        logger.info("{} ID'li kullanıcının beğenileri getiriliyor. Sayfa: {}, Boyut: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Like> likes = likeRepository.findByUserIdAndStatusTrue(userId, pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            logger.info("{} ID'li kullanıcının {} beğenisi başarıyla getirildi", userId, likeDTOs.getTotalElements());
            return new SuccessDataResult<>(likeDTOs, "Kullanıcı beğenileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcının beğenileri getirilirken bir hata oluştu", userId, e);
            return new ErrorDataResult<>(null, "Kullanıcı beğenileri getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<LikeDTO> createLike(LikeDTO likeDTO) {
        logger.info("Yeni beğeni oluşturuluyor");
        try {
            Like like = likeMapper.toEntity(likeDTO);
            like.setStatus(true);
            Like savedLike = likeRepository.save(like);
            LikeDTO savedLikeDTO = likeMapper.toDTO(savedLike);
            logger.info("{} ID'li yeni beğeni başarıyla oluşturuldu", savedLikeDTO.getId());
            return new SuccessDataResult<>(savedLikeDTO, "Beğeni başarıyla oluşturuldu");
        } catch (Exception e) {
            logger.error("Beğeni oluşturulurken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Beğeni oluşturulurken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public Result softDeleteLike(Long id) {
        logger.info("{} ID'li beğeni siliniyor (soft delete)", id);
        try {
            Like like = likeRepository.findById(id).orElse(null);
            if (like != null) {
                like.setStatus(false);
                likeRepository.save(like);
                logger.info("{} ID'li beğeni başarıyla silindi (soft delete)", id);
                return new SuccessResult("Beğeni başarıyla silindi");
            } else {
                logger.warn("{} ID'li beğeni bulunamadı", id);
                return new ErrorResult("Silinecek beğeni bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li beğeni silinirken bir hata oluştu", id, e);
            return new ErrorResult("Beğeni silinirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<LikeDTO> updateLikeStatus(Long id, boolean status) {
        logger.info("{} ID'li beğeninin durumu güncelleniyor. Yeni durum: {}", id, status);
        try {
            Like like = likeRepository.findById(id).orElse(null);
            if (like != null) {
                like.setStatus(status);
                Like updatedLike = likeRepository.save(like);
                LikeDTO updatedLikeDTO = likeMapper.toDTO(updatedLike);
                logger.info("{} ID'li beğeninin durumu başarıyla güncellendi", id);
                return new SuccessDataResult<>(updatedLikeDTO, "Beğeni durumu başarıyla güncellendi");
            } else {
                logger.warn("{} ID'li beğeni bulunamadı", id);
                return new ErrorDataResult<>(null, "Güncellenecek beğeni bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li beğeninin durumu güncellenirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Beğeni durumu güncellenirken bir hata oluştu");
        }
    }
}