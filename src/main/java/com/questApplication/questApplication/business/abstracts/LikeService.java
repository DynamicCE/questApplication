package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public interface LikeService {
    Page<LikeResponseDto> getAllLikes(Pageable pageable);

    Page<LikeResponseDto> getLikesByPostId(Long postId, Pageable pageable);

    void createLike(LikeRequestDto likeRequestDto, String username);

    long getLikeCountForPost(Long postId);
}
