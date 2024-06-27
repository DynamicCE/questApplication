package com.questApplication.questApplication.repository;

import com.questApplication.questApplication.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public
interface LikeRepository extends JpaRepository<Like,Long> {
}
