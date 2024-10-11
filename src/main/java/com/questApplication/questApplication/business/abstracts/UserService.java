package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    UserResponseDto getUserById(Long id);
    UserResponseDto getUserByUsername(String username);
    void createUser( UserRequestDto userRequestDto);
    void updateUser(Long id, UserRequestDto UserRequestDto,String username);
    void deleteUser(Long id,String username);
    void activateUser(Long id,String username);
}