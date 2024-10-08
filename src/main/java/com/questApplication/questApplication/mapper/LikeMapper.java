package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Like;
import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMapper.class})
public interface LikeMapper {

    @Mapping(target = "user", ignore = true) // Kullanıcı bilgisi oturumdan alınacak
    @Mapping(target = "post", ignore = true) // Post bilgisi servisten alınacak
    @Mapping(target = "comment", ignore = true) // Yorum bilgisini servisten alınacak
    Like toEntity(LikeRequestDto likeRequestDto);

    LikeResponseDto toResponseDto(Like like);
}
