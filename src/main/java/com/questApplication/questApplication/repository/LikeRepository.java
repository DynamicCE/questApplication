package com.questApplication.questApplication.repository;

import com.questApplication.questApplication.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Page<Like> findByPostId(Long postId, Pageable pageable);
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    Page<Like> findByCommentId ( Long commentId, Pageable pageable );

    Optional<Like> findByPostIdAndUserId ( Long id, Long id1 );

    Optional<Like> findByCommentIdAndUserId ( Long id, Long id1 );
}