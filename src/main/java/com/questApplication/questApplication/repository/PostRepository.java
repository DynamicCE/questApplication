package com.questApplication.questApplication.repository;

import com.questApplication.questApplication.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByStatusNot(String status, Pageable pageable);
    Optional<Post> findByIdAndStatusNot(Long id, String status);
    Page<Post> findByUserIdAndStatusNot(Long userId, String status, Pageable pageable);
    boolean existsByIdAndStatusNot ( Long postId, String d );

    List<Post> findTop10ByStatusNotOrderByLikeCountDesc ( String d );
}