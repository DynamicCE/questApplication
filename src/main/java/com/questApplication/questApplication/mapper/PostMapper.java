package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.dto.request.PostRequestDto;
import com.questApplication.questApplication.entity.dto.response.PostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper {
    Post toEntity(PostRequestDto postRequestDto);

    PostResponseDto toResponseDto(Post post);
}

