package com.questApplication.questApplication.repository;

import com.questApplication.questApplication.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByStatusNot(String status, Pageable pageable);
    Optional<Comment> findByIdAndStatusNot(Long id, String status);
    Page<Comment> findByPostIdAndStatusNot(Long postId, String status, Pageable pageable);
    Page<Comment> findByUserIdAndStatusNot(Long userId, String status, Pageable pageable);
}