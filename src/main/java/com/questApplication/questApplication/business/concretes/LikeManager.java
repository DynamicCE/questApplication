package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import com.questApplication.questApplication.mapper.LikeMapper;
import com.questApplication.questApplication.repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeManager implements LikeService {
    private static final Logger logger = LoggerFactory.getLogger(LikeManager.class);
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    public LikeManager(LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public DataResult<Page<LikeDTO>> getAllLikes(Pageable pageable) {
        logger.info("Tüm beğeniler getiriliyor, Sayfa: {}, Boyut: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Like> likes = likeRepository.findAll(pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            logger.info("Toplam {} beğeni başarıyla getirildi", likeDTOs.getTotalElements());
            return new SuccessDataResult<>(likeDTOs, "Tüm beğeniler başarıyla getirildi");
        } catch (Exception e) {
            logger.error("Beğeniler getirilirken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Beğeniler getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<LikeDTO>> getLikesByPostId(Long postId, Pageable pageable) {
        logger.info("{} ID'li gönderinin beğenileri getiriliyor, Sayfa: {}, Boyut: {}", postId, pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Like> likes = likeRepository.findByPostId(postId, pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            logger.info("{} ID'li gönderinin {} beğenisi başarıyla getirildi", postId, likeDTOs.getTotalElements());
            return new SuccessDataResult<>(likeDTOs, "Gönderi beğenileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li gönderinin beğenileri getirilirken bir hata oluştu", postId, e);
            return new ErrorDataResult<>(null, "Gönderi beğenileri getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public Result createLike(LikeDTO likeDTO) {
        logger.info("Yeni beğeni oluşturuluyor");
        try {
            if (likeRepository.existsByPostIdAndUserId(likeDTO.getPostId(), likeDTO.getUserId())) {
                logger.warn("Bu gönderi zaten bu kullanıcı tarafından beğenilmiş");
                return new ErrorResult("Bu gönderiyi zaten beğenmişsiniz");
            }
            Like like = likeMapper.toEntity(likeDTO);
            likeRepository.save(like);
            logger.info("Yeni beğeni başarıyla oluşturuldu");
            return new SuccessResult("Beğeni başarıyla oluşturuldu");
        } catch (Exception e) {
            logger.error("Beğeni oluşturulurken bir hata oluştu", e);
            return new ErrorResult("Beğeni oluşturulurken bir hata oluştu");        }
    }

    @Override
    public DataResult<Long> getLikeCountForPost(Long postId) {
        logger.info("{} ID'li gönderinin beğeni sayısı getiriliyor", postId);
        try {
            long likeCount = likeRepository.countByPostId(postId);
            logger.info("{} ID'li gönderinin beğeni sayısı: {}", postId, likeCount);
            return new SuccessDataResult<>(likeCount, "Gönderi beğeni sayısı başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li gönderinin beğeni sayısı getirilirken bir hata oluştu", postId, e);
            return new ErrorDataResult<>(null, "Gönderi beğeni sayısı getirilirken bir hata oluştu");
        }
    }


}