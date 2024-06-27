package com.questApplication.questApplication.repository;

import com.questApplication.questApplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByStatusNot(String status, Pageable pageable);
    Optional<User> findByIdAndStatusNot(Long id, String status);
    Optional<User> findByUsernameAndStatusNot(String username, String status);
    boolean existsByUsername(String username);
}