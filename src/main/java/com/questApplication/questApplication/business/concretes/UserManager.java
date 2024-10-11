package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.core.utilities.exception.UnauthorizedException;
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
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
        return userMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsernameAndStatusNot(username, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));
        return userMapper.toResponseDto(user);
    }

    @Override
    public void createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new IllegalArgumentException("Kullanıcı adı zaten alınmış");
        }

        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setStatus("A"); // Aktif durum
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long id, UserRequestDto userRequestDto, String username) {
        User existingUser = userRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));

        if (!existingUser.getUsername().equals(username)) {
            throw new UnauthorizedException("Bu kullanıcıyı güncelleme yetkiniz yok");
        }

        existingUser.setUsername(userRequestDto.getUsername());
        existingUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        existingUser.setStatus("U"); // Güncellenmiş durum
        userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id, String username) {
        User user = userRepository.findByIdAndStatusNot(id, "D")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı"));

        if (!user.getUsername().equals(username)) {
            throw new UnauthorizedException("Bu kullanıcıyı silme yetkiniz yok");
        }

        user.setStatus("D"); // Silinmiş durum
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long id, String username) {
        User user = userRepository.findByIdAndStatusNot(id, "A")
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı veya zaten aktif"));

        if (!user.getUsername().equals(username)) {
            throw new UnauthorizedException("Bu kullanıcıyı aktifleştirme yetkiniz yok");
        }

        user.setStatus("A"); // Aktif durum
        userRepository.save(user);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAllByStatusNot("D", pageable);
        return users.map(userMapper::toResponseDto);
    }
}
