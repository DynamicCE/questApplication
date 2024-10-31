package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByUsername(String username);

    void updateUser(Long id, UserRequestDto userRequestDto, String username);

    void deleteUser(Long id, String username);

    void activateUser(Long id, String username);

    Page<UserResponseDto> getAllUsers(Pageable pageable);
}
