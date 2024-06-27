package com.questApplication.questApplication.mapper;

import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
}