package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.core.utilities.results.DataResult;
import com.questApplication.questApplication.core.utilities.results.Result;
import com.questApplication.questApplication.entity.dto.UserDTO;

import java.util.List;

public interface UserService {
    DataResult<List<UserDTO>> getAllUsers();
    DataResult<UserDTO> getUserById(Long id);
    DataResult<UserDTO> createUser(UserDTO userDTO);
    DataResult<UserDTO> updateUser(Long id, UserDTO userDTO);
    Result deleteUser(Long id);
}