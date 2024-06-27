package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.entity.dto.PostDTO;

import java.util.List;

public interface PostService {
    DataResult<List<PostDTO>> getAllPosts();
    DataResult<PostDTO> getPostById(Long id);
    DataResult<List<PostDTO>> getPostsByUserId(Long userId);
    DataResult<PostDTO> createPost(PostDTO postDTO);
    DataResult<PostDTO> updatePost(Long id, PostDTO postDTO);
    Result deletePost(Long id);
}