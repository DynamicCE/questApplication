package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikeDTO toDTO( Like like);
    Like toEntity(LikeDTO likeDTO);
}