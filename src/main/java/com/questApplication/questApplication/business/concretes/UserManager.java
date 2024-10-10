package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.UserResponseDto;
import com.questApplication.questApplication.mapper.UserMapper;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManager implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserManager(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public
    UserResponseDto getUserById ( Long id ) {
        return null;
    }

    @Override
    public
    UserResponseDto getUserByUsername ( String username ) {
        return null;
    }

    @Override
    public
    void createUser ( UserRequestDto userRequestDto ) {

    }

    @Override
    public
    void updateUser ( Long id, UserRequestDto UserRequestDto, String username ) {

    }

    @Override
    public
    void deleteUser ( Long id, String username ) {

    }

    @Override
    public
    void activateUser ( Long id, String username ) {

    }
}
