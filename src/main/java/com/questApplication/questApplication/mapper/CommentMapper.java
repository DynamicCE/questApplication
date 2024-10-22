package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class, PostMapper.class })
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user", ignore = true)
    Comment toEntity(CommentRequestDto commentRequestDto);

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "likedByCurrentUser", ignore = true)
    CommentResponseDto toResponseDto(Comment comment);
}