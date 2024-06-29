package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    DataResult<Page<LikeDTO>> getAllLikes ( Pageable pageable );
    DataResult<Page<LikeDTO>> getLikesByPostId(Long postId, Pageable pageable);
    Result createLike(LikeDTO likeDTO);
    DataResult<Long> getLikeCountForPost(Long postId);
}