package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.UserDTO;
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
    public DataResult<Page<UserDTO>> getAllUsers(Pageable pageable) {
        try {
            Page<User> users = userRepository.findAllByStatusNot("D", pageable);
            Page<UserDTO> userDTOs = users.map(userMapper::toDTO);
            return new SuccessDataResult<>(userDTOs, "Kullanıcılar başarıyla getirildi");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcılar getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<UserDTO> getUserById(Long id) {
        try {
            User user = userRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (user != null) {
                UserDTO userDTO = userMapper.toDTO(user);
                return new SuccessDataResult<>(userDTO, "Kullanıcı başarıyla getirildi");
            } else {
                return new ErrorDataResult<>(null, "Kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcı getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<UserDTO> createUser(UserDTO userDTO) {
        try {
            if (userRepository.existsByUsername(userDTO.getUsername())) {
                return new ErrorDataResult<>(null, "Bu kullanıcı adı zaten kullanılıyor");
            }

            User user = userMapper.toEntity(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setStatus("A"); // Active
            User savedUser = userRepository.save(user);
            UserDTO savedUserDTO = userMapper.toDTO(savedUser);
            return new SuccessDataResult<>(savedUserDTO, "Kullanıcı başarıyla oluşturuldu");
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcı oluşturulurken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<UserDTO> updateUser(Long id, UserDTO userDTO) {
        try {
            User existingUser = userRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (existingUser != null) {
                User updatedUser = userMapper.toEntity(userDTO);
                updatedUser.setId(id);
                updatedUser.setStatus("U");

                if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                    updatedUser.setPassword(existingUser.getPassword());
                } else {
                    updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                }

                User savedUser = userRepository.save(updatedUser);
                UserDTO savedUserDTO = userMapper.toDTO(savedUser);
                return new SuccessDataResult<>(savedUserDTO, "Kullanıcı başarıyla güncellendi");
            } else {
                return new ErrorDataResult<>(null, "Güncellenecek kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcı güncellenirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public Result deleteUser(Long id) {
        try {
            User user = userRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (user != null) {
                user.setStatus("D");
                userRepository.save(user);
                return new SuccessResult("Kullanıcı başarıyla silindi");
            } else {
                return new ErrorResult("Silinecek kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorResult("Kullanıcı silinirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<UserDTO> getUserByUsername(String username) {
        try {
            User user = userRepository.findByUsernameAndStatusNot(username, "D").orElse(null);
            if (user != null) {
                UserDTO userDTO = userMapper.toDTO(user);
                return new SuccessDataResult<>(userDTO, "Kullanıcı başarıyla getirildi");
            } else {
                return new ErrorDataResult<>(null, "Kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcı getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<UserDTO> activateUser(Long id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                if (!user.getStatus().equals("A")) {
                    user.setStatus("A");
                    User savedUser = userRepository.save(user);
                    UserDTO activatedUserDTO = userMapper.toDTO(savedUser);
                    return new SuccessDataResult<>(activatedUserDTO, "Kullanıcı başarıyla aktifleştirildi");
                } else {
                    return new ErrorDataResult<>(null, "Kullanıcı zaten aktif");
                }
            } else {
                return new ErrorDataResult<>(null, "Aktifleştirilecek kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            return new ErrorDataResult<>(null, "Kullanıcı aktifleştirilirken bir hata oluştu");
        }
    }
}
