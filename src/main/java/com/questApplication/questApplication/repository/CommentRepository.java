package com.questApplication.questApplication.repository;

import com.questApplication.questApplication.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface CommentRepository extends JpaRepository<Comment,Long> {
}
