package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.Post;
import com.questApplication.questApplication.entity.dto.PostDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LikeMapper.class, CommentMapper.class})
public interface PostMapper {
    PostDTO toDTO(Post post);
    Post toEntity(PostDTO postDTO);
}