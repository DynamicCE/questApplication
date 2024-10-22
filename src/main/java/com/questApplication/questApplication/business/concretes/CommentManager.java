package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.core.utilities.exception.UnauthorizedException;
import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import com.questApplication.questApplication.mapper.CommentMapper;
import com.questApplication.questApplication.repository.CommentRepository;
import com.questApplication.questApplication.repository.PostRepository;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentManager implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentManager(CommentRepository commentRepository, CommentMapper commentMapper,
                          UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Page<CommentResponseDto> getCommentsByPostId(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostIdAndStatusNot(postId, "D", pageable);
        return comments.map(commentMapper::toResponseDto);
    }

    @Override
    public
    Page<CommentResponseDto> getCommentsByCommentId ( Long commentId, Pageable pageable ) {
        Page<Comment> comments = commentRepository.findByIdAndStatusNot(commentId, "D", pageable);
        return comments.map(commentMapper::toResponseDto);
    }

    @Override
    public Page<CommentResponseDto> getCommentsByCurrentUser(String username, Pageable pageable) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
        Page<Comment> comments = commentRepository.findByUserIdAndStatusNot(user.getId(), "D", pageable);
        return comments.map(commentMapper::toResponseDto);
    }

    @Override
    @Transactional
    public void createComment(CommentRequestDto commentRequestDto, String username) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı")); // yönetici tarafından silindigi halde oturum açık kalmışken işlem yapamaması için.

        Post post = postRepository.findByIdAndStatusNot(commentRequestDto.getPostId(), "D") // yorum yapılacak gönderi silindiyse.
                .orElseThrow(() -> new ResourceNotFoundException("Gönderi bulunamadı"));

        Comment parentComment = null;
        if(commentRequestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findByIdAndStatusNot(commentRequestDto.getParentCommentId(), "D")
                .orElseThrow(() -> new ResourceNotFoundException("Üst yorum bulunamadı"));
        }

        Comment comment = commentMapper.toEntity(commentRequestDto);
        comment.setUser(user);
        comment.setPost(post);
        comment.setParentComment(parentComment); 
        comment.setStatus("A");

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void updateComment(Long id, CommentRequestDto commentRequestDto, String username) {
        Comment existingComment = commentRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı"));

        if (!existingComment.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Bu yorumu güncelleme yetkiniz yok");
        }

        existingComment.setText(commentRequestDto.getText());
        existingComment.setStatus("U");

        commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id, String username) {
        Comment comment = commentRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Yorum bulunamadı"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Bu yorumu silme yetkiniz yok");
        }

        comment.setStatus("D");
        commentRepository.save(comment);
    }
}
