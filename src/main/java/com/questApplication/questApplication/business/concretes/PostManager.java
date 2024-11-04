package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.core.utilities.exception.UnauthorizedException;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.PostRequestDto;
import com.questApplication.questApplication.entity.dto.response.PostResponseDto;
import com.questApplication.questApplication.entity.elastic.PostDocument;
import com.questApplication.questApplication.mapper.PostMapper;
import com.questApplication.questApplication.repository.PostRepository;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.questApplication.questApplication.repository.elastic.PostElasticsearchRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PostManager implements PostService {

    private final RedisTemplate<String, List<Post>> redisTemplate;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final PostElasticsearchRepository postElasticsearchRepository;

    public PostManager(RedisTemplate<String, List<Post>> redisTemplate,
            PostRepository postRepository,
            PostMapper postMapper,
            UserRepository userRepository,
            PostElasticsearchRepository postElasticsearchRepository) {
        this.redisTemplate = redisTemplate;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
        this.postElasticsearchRepository = postElasticsearchRepository;
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
    public Long createPost(PostRequestDto postRequestDto, String username) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));

        Post post = postMapper.toEntity(postRequestDto);
        post.setUser(user);
        post.setStatus("A");

        postRepository.save(post);
        clearTopLikedPostsCache();

        PostDocument postDocument = new PostDocument();
        postDocument.setId(post.getId().toString());
        postDocument.setTitle(post.getTitle());
        postDocument.setText(post.getText());
        postDocument.setUsername(username);
        postDocument.setLikeCount(0L);
        postElasticsearchRepository.save(postDocument);

        return post.getId();
    }

    @Override
    @Transactional
    public void updatePost(Long id, PostRequestDto postRequestDto, String username) {
        Post existingPost = postRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));

        if (!existingPost.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Bu gönderiyi güncelleme yetkiniz yok");
        }

        existingPost.setTitle(postRequestDto.getTitle());
        existingPost.setText(postRequestDto.getText());
        existingPost.setStatus("U");

        postRepository.save(existingPost);
        clearTopLikedPostsCache();
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
        clearTopLikedPostsCache();
    }

    @Override
    public List<PostResponseDto> getTopLikedPosts() {
        String redisKey = "topLikedPosts";
        List<Post> posts = redisTemplate.opsForValue().get(redisKey);

        if (posts == null) {
            posts = postRepository.findTop10ByStatusNotOrderByLikeCountDesc("D");
            redisTemplate.opsForValue().set(redisKey, posts, 1, TimeUnit.HOURS);
        }

        return posts.stream()
                .map(postMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 3600000)
    public void updateTopLikedPostsCache() {
        List<Post> topPosts = postRepository.findTop10ByStatusNotOrderByLikeCountDesc("D");
        String redisKey = "topLikedPosts";
        redisTemplate.opsForValue().set(redisKey, topPosts, 1, TimeUnit.HOURS);
    }

    private void clearTopLikedPostsCache() {
        String redisKey = "topLikedPosts";
        redisTemplate.delete(redisKey);
    }
}
