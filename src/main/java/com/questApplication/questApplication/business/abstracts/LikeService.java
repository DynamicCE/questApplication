package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.entity.dto.LikeDTO;

import java.util.List;

public interface LikeService {
    DataResult<List<LikeDTO>> getAllLikes();
    DataResult<LikeDTO> getLikeById(Long id);
    DataResult<List<LikeDTO>> getLikesByPostId(Long postId);
    DataResult<List<LikeDTO>> getLikesByUserId(Long userId);
    DataResult<LikeDTO> createLike(LikeDTO likeDTO);
    Result deleteLike(Long id);
}