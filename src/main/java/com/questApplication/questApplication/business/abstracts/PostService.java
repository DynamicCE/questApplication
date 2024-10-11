package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.PostRequestDto;
import com.questApplication.questApplication.entity.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public interface PostService {
    Page<PostResponseDto> getAllPosts(Pageable pageable);
    PostResponseDto getPostById(Long id);
    Page<PostResponseDto> getPostsByUser(String username, Pageable pageable);
    void createPost(PostRequestDto postRequestDto, String username);
    void updatePost(Long id, PostRequestDto postRequestDto, String username);
    void deletePost(Long id, String username);
}
