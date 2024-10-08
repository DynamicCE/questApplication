package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    Page<LikeResponseDto> getAllLikes(Pageable pageable);

    Page<LikeResponseDto> getLikesByPostId(Long postId, Pageable pageable);

    LikeResponseDto createLike(LikeRequestDto likeRequestDto, String username);

    long getLikeCountForPost(Long postId);
}
