package com.questApplication.questApplication.repository;


import com.questApplication.questApplication.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Page<Like> findAllByStatusTrue(Pageable pageable);
    Optional<Like> findByIdAndStatusTrue(Long id);
    Page<Like> findByPostIdAndStatusTrue(Long postId, Pageable pageable);
    Page<Like> findByUserIdAndStatusTrue(Long userId, Pageable pageable);
}