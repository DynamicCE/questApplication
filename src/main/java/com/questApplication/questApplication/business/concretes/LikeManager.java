package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.core.utilities.results.SuccessDataResult;
import com.questApplication.questApplication.core.utilities.results.ErrorDataResult;
import com.questApplication.questApplication.core.utilities.results.SuccessResult;
import com.questApplication.questApplication.core.utilities.results.ErrorResult;
import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.LikeDTO;

import com.questApplication.questApplication.mapper.LikeMapper;
import com.questApplication.questApplication.repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeManager implements LikeService {
    private static final Logger logger = LoggerFactory.getLogger(LikeManager.class);
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Autowired
    public LikeManager(LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public DataResult<List<LikeDTO>> getAllLikes() {
        logger.info("Tüm beğeniler getiriliyor");
        try {
            List<Like> likes = likeRepository.findAll();
            List<LikeDTO> likeDTOs = likes.stream()
                    .map(likeMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Toplam {} beğeni başarıyla getirildi", likeDTOs.size());
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
            Like like = likeRepository.findById(id).orElse(null);
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
    public DataResult<List<LikeDTO>> getLikesByPostId(Long postId) {
        logger.info("{} ID'li gönderinin beğenileri getiriliyor", postId);
        try {
            List<Like> likes = likeRepository.findByPostId(postId);
            List<LikeDTO> likeDTOs = likes.stream()
                    .map(likeMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("{} ID'li gönderinin {} beğenisi başarıyla getirildi", postId, likeDTOs.size());
            return new SuccessDataResult<>(likeDTOs, "Gönderi beğenileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li gönderinin beğenileri getirilirken bir hata oluştu", postId, e);
            return new ErrorDataResult<>(null, "Gönderi beğenileri getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<List<LikeDTO>> getLikesByUserId(Long userId) {
        logger.info("{} ID'li kullanıcının beğenileri getiriliyor", userId);
        try {
            List<Like> likes = likeRepository.findByUserId(userId);
            List<LikeDTO> likeDTOs = likes.stream()
                    .map(likeMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("{} ID'li kullanıcının {} beğenisi başarıyla getirildi", userId, likeDTOs.size());
            return new SuccessDataResult<>(likeDTOs, "Kullanıcı beğenileri başarıyla getirildi");
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcının beğenileri getirilirken bir hata oluştu", userId, e);
            return new ErrorDataResult<>(null, "Kullanıcı beğenileri getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<LikeDTO> createLike(LikeDTO likeDTO) {
        logger.info("Yeni beğeni oluşturuluyor");
        try {
            Like like = likeMapper.toEntity(likeDTO);
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
    public Result deleteLike(Long id) {
        logger.info("{} ID'li beğeni siliniyor", id);
        try {
            if (likeRepository.existsById(id)) {
                likeRepository.deleteById(id);
                logger.info("{} ID'li beğeni başarıyla silindi", id);
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
}