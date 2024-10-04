package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import com.questApplication.questApplication.mapper.LikeMapper;
import com.questApplication.questApplication.repository.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeManager implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    public LikeManager(LikeRepository likeRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
    }

    @Override
    public DataResult<Page<LikeDTO>> getAllLikes(Pageable pageable) {
        try {
            Page<Like> likes = likeRepository.findAll(pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            return new SuccessDataResult<>(likeDTOs, "Tüm beğeniler başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Beğeniler getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Page<LikeDTO>> getLikesByPostId(Long postId, Pageable pageable) {
        try {
            Page<Like> likes = likeRepository.findByPostId(postId, pageable);
            Page<LikeDTO> likeDTOs = likes.map(likeMapper::toDTO);
            return new SuccessDataResult<>(likeDTOs, "Gönderi beğenileri başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderi beğenileri getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public Result createLike(LikeDTO likeDTO) {
        try {
            if (likeRepository.existsByPostIdAndUserId(likeDTO.getPostId(), likeDTO.getUserId())) {
                return new ErrorResult("Bu gönderiyi zaten beğenmişsiniz");
            }
            Like like = likeMapper.toEntity(likeDTO);
            likeRepository.save(like);
            return new SuccessResult("Beğeni başarıyla oluşturuldu");
        } catch (Exception e) {
            return new ErrorResult("Beğeni oluşturulurken bir hata oluştu");
        }
    }

    @Override
    public DataResult<Long> getLikeCountForPost(Long postId) {
        try {
            long likeCount = likeRepository.countByPostId(postId);
            return new SuccessDataResult<>(likeCount, "Gönderi beğeni sayısı başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Gönderi beğeni sayısı getirilirken bir hata oluştu");
        }
    }
}
