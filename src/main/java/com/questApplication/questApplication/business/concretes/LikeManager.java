package com.questApplication.questApplication.service.concretes;

import com.questApplication.questApplication.service.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import com.questApplication.questApplication.mapper.LikeMapper;
import com.questApplication.questApplication.repository.CommentRepository;
import com.questApplication.questApplication.repository.LikeRepository;
import com.questApplication.questApplication.repository.PostRepository;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeManager implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public LikeManager(LikeRepository likeRepository, LikeMapper likeMapper,
                       UserRepository userRepository, PostRepository postRepository,CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.likeMapper = likeMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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
    public Page<LikeResponseDto> getLikesByCommentId(Long commentId, Pageable pageable) {
        if (!commentRepository.existsByIdAndStatusNot(commentId, "D")) {
            throw new ResourceNotFoundException("Yorum bulunamadı");
        }
        Page<Like> likes = likeRepository.findByCommentId(commentId, pageable);
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

    @Override
    public
    void deleteLike ( Long id, String username ) {

    }

    @Override
    @Transactional
    public void toggleLike(LikeRequestDto likeRequestDto, String username) {
        User user = getUserByUsername(username);

        if (likeRequestDto.getPostId() != null) {
            handlePostLike(likeRequestDto.getPostId(), user);
        } else if (likeRequestDto.getCommentId() != null) {
            handleCommentLike(likeRequestDto.getCommentId(), user);
        }
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
    }

    private void handlePostLike(Long postId, User user) {
        Post post = postRepository.findByIdAndStatusNot(postId, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());
        if (existingLike.isPresent()) {
            unlikePost(existingLike.get(), post);
        } else {
            likePost(post, user);
        }
    }

    private void handleCommentLike(Long commentId, User user) {
        Comment comment = commentRepository.findByIdAndStatusNot(commentId, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı"));

        Optional<Like> existingLike = likeRepository.findByCommentIdAndUserId(comment.getId(), user.getId());
        if (existingLike.isPresent()) {
            unlikeComment(existingLike.get(), comment);
        } else {
            likeComment(comment, user);
        }
    }

    private void likePost(Post post, User user) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setStatus("A");
        likeRepository.save(like);
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    private void unlikePost(Like like, Post post) {
        likeRepository.delete(like);
        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
    }

    private void likeComment(Comment comment, User user) {
        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        like.setStatus("A");
        likeRepository.save(like);
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    private void unlikeComment(Like like, Comment comment) {
        likeRepository.delete(like);
        comment.setLikeCount(comment.getLikeCount() - 1);
        commentRepository.save(comment);
    }
}
