package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.entity.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    DataResult<Page<PostDTO>> getAllPosts(Pageable pageable);
    DataResult<PostDTO> getPostById(Long id);
    DataResult<Page<PostDTO>> getPostsByUserId(Long userId, Pageable pageable);
    DataResult<PostDTO> createPost(PostDTO postDTO);
    DataResult<PostDTO> updatePost(Long id, PostDTO postDTO);
    Result deletePost(Long id);
    DataResult<PostDTO> activatePost(Long id);
}