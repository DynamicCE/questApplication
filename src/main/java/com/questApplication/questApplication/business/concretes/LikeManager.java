package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import com.questApplication.questApplication.mapper.LikeMapper;
import com.questApplication.questApplication.repository.LikeRepository;
import com.questApplication.questApplication.repository.PostRepository;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeManager implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeManager(LikeRepository likeRepository, LikeMapper likeMapper,
                       UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Page<LikeResponseDto> getAllLikes(Pageable pageable) {
        Page<Like> likes = likeRepository.findAll(pageable);
        return likes.map(likeMapper::toResponseDto);
    }

    @Override
    public Page<LikeResponseDto> getLikesByPostId(Long postId, Pageable pageable) {
        if (!postRepository.existsByIdAndStatusNot(postId, "D")) {
            throw new ResourceNotFoundException("Gönderi bulunamadı");
        }
        Page<Like> likes = likeRepository.findByPostId(postId, pageable);
        return likes.map(likeMapper::toResponseDto);
    }

    @Override
    @Transactional
    public void createLike(LikeRequestDto likeRequestDto, String username) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));

        Post post = postRepository.findByIdAndStatusNot(likeRequestDto.getPostId(), "D")
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));

        if (likeRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            throw new IllegalArgumentException("Bu gönderiyi zaten beğenmişsiniz");
        }

        Like like = likeMapper.toEntity(likeRequestDto);
        like.setUser(user);
        like.setPost(post);

        likeRepository.save(like);
    }

    @Override
    public long getLikeCountForPost(Long postId) {
        if (!postRepository.existsByIdAndStatusNot(postId, "D")) {
            throw new ResourceNotFoundException("Gönderi bulunamadı");
        }
        return likeRepository.countByPostId(postId);
    }
}
