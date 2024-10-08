package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentRequestDto commentRequestDto);

    CommentResponseDto toResponseDto(Comment comment);
}
