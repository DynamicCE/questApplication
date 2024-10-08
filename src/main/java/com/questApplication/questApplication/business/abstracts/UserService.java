package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    DataResult<Page<UserDTO>> getAllUsers(Pageable pageable);
    DataResult<UserDTO> getUserById(Long id);
    DataResult<UserDTO> createUser(UserDTO userDTO);
    DataResult<UserDTO> updateUser(Long id, UserDTO userDTO);
    Result deleteUser(Long id);
    DataResult<UserDTO> getUserByUsername(String username);
    DataResult<UserDTO> activateUser(Long id);
}