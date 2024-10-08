package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.core.utilities.exception.UnauthorizedException;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.PostRequestDto;
import com.questApplication.questApplication.entity.dto.response.PostResponseDto;
import com.questApplication.questApplication.mapper.PostMapper;
import com.questApplication.questApplication.repository.PostRepository;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostManager implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Autowired
    public PostManager(PostRepository postRepository, PostMapper postMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByStatusNot("D", pageable);
        return posts.map(postMapper::toResponseDto);
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));
        return postMapper.toResponseDto(post);
    }

    @Override
    public Page<PostResponseDto> getPostsByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
        Page<Post> posts = postRepository.findByUserIdAndStatusNot(user.getId(), "D", pageable);
        return posts.map(postMapper::toResponseDto);
    }

    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));

        Post post = postMapper.toEntity(postRequestDto);
        post.setUser(user);
        post.setStatus("A");

        Post savedPost = postRepository.save(post);
        return postMapper.toResponseDto(savedPost);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, String username) {
        Post existingPost = postRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));

        if (!existingPost.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Bu gönderiyi güncelleme yetkiniz yok");
        }

        existingPost.setTitle(postRequestDto.getTitle());
        existingPost.setText(postRequestDto.getText());
        existingPost.setStatus("U");

        Post updatedPost = postRepository.save(existingPost);
        return postMapper.toResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id, String username) {
        Post post = postRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));

        if (!post.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Bu gönderiyi silme yetkiniz yok");
        }

        post.setStatus("D");
        postRepository.save(post);
    }
}
