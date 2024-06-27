package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Comment;
import com.questApplication.questApplication.entity.dto.CommentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDTO toDTO( Comment comment);
    Comment toEntity(CommentDTO commentDTO);
}