package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDto> getAllUsers( Pageable pageable);
    UserResponseDto getUserById(Long id);
    UserResponseDto getUserByUsername(String username);
    void createUser( UserRequestDto userRequestDto);
    void updateUser(Long id, UserRequestDto UserRequestDto);
    void deleteUser(Long id);
    void activateUser(Long id);
}