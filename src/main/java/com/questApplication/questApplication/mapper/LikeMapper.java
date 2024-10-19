package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMapper.class, CommentMapper.class})
public interface LikeMapper {
    Like toEntity(LikeRequestDto likeRequestDto);

    LikeResponseDto toResponseDto(Like like);
}

