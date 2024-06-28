package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.result.*;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.UserDTO;
import com.questApplication.questApplication.mapper.UserMapper;
import com.questApplication.questApplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManager implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
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
        logger.info("Tüm kullanıcılar getiriliyor. Sayfa: {}, Boyut: {}", pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<User> users = userRepository.findAllByStatusNot("D", pageable);
            Page<UserDTO> userDTOs = users.map(userMapper::toDTO);
            logger.info("Toplam {} kullanıcı başarıyla getirildi", userDTOs.getTotalElements());
            return new SuccessDataResult<>(userDTOs, "Kullanıcılar başarıyla getirildi");
        } catch (Exception e) {
            logger.error("Kullanıcılar getirilirken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Kullanıcılar getirilirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<UserDTO> getUserById(Long id) {
        logger.info("{} ID'li kullanıcı getiriliyor", id);
        try {
            User user = userRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (user != null) {
                UserDTO userDTO = userMapper.toDTO(user);
                logger.info("{} ID'li kullanıcı başarıyla getirildi", id);
                return new SuccessDataResult<>(userDTO, "Kullanıcı başarıyla getirildi");
            } else {
                logger.warn("{} ID'li kullanıcı bulunamadı", id);
                return new ErrorDataResult<>(null, "Kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcı getirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Kullanıcı getirilirken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<UserDTO> createUser(UserDTO userDTO) {
        logger.info("Yeni kullanıcı oluşturuluyor");
        try {
            if (userRepository.existsByUsername(userDTO.getUsername())) {
                logger.warn("Bu kullanıcı adı zaten kullanılıyor: {}", userDTO.getUsername());
                return new ErrorDataResult<>(null, "Bu kullanıcı adı zaten kullanılıyor");
            }

            User user = userMapper.toEntity(userDTO);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setStatus("A"); // Active
            User savedUser = userRepository.save(user);
            UserDTO savedUserDTO = userMapper.toDTO(savedUser);
            logger.info("{} ID'li yeni kullanıcı başarıyla oluşturuldu", savedUserDTO.getId());
            return new SuccessDataResult<>(savedUserDTO, "Kullanıcı başarıyla oluşturuldu");
        } catch (Exception e) {
            logger.error("Kullanıcı oluşturulurken bir hata oluştu", e);
            return new ErrorDataResult<>(null, "Kullanıcı oluşturulurken bir hata oluştu");
        }
    }

    @Override
    @Transactional
    public DataResult<UserDTO> updateUser(Long id, UserDTO userDTO) {
        logger.info("{} ID'li kullanıcı güncelleniyor", id);
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
                logger.info("{} ID'li kullanıcı başarıyla güncellendi", id);
                return new SuccessDataResult<>(savedUserDTO, "Kullanıcı başarıyla güncellendi");
            } else {
                logger.warn("{} ID'li kullanıcı bulunamadı", id);
                return new ErrorDataResult<>(null, "Güncellenecek kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcı güncellenirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Kullanıcı güncellenirken bir hata oluştu");
        }
    }


    @Override
    @Transactional
    public Result deleteUser(Long id) {
        logger.info("{} ID'li kullanıcı siliniyor (soft delete)", id);
        try {
            User user = userRepository.findByIdAndStatusNot(id, "D").orElse(null);
            if (user != null) {
                user.setStatus("D");
                userRepository.save(user);
                logger.info("{} ID'li kullanıcı başarıyla silindi (soft delete)", id);
                return new SuccessResult("Kullanıcı başarıyla silindi");
            } else {
                logger.warn("{} ID'li kullanıcı bulunamadı", id);
                return new ErrorResult("Silinecek kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcı silinirken bir hata oluştu", id, e);
            return new ErrorResult("Kullanıcı silinirken bir hata oluştu");
        }
    }

    @Override
    public DataResult<UserDTO> getUserByUsername(String username) {
        logger.info("{} kullanıcı adına sahip kullanıcı getiriliyor", username);
        try {
            User user = userRepository.findByUsernameAndStatusNot(username, "D").orElse(null);
            if (user != null) {
                UserDTO userDTO = userMapper.toDTO(user);
                logger.info("{} kullanıcı adına sahip kullanıcı başarıyla getirildi", username);
                return new SuccessDataResult<>(userDTO, "Kullanıcı başarıyla getirildi");
            } else {
                logger.warn("{} kullanıcı adına sahip kullanıcı bulunamadı", username);
                return new ErrorDataResult<>(null, "Kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} kullanıcı adına sahip kullanıcı getirilirken bir hata oluştu", username, e);
            return new ErrorDataResult<>(null, "Kullanıcı getirilirken bir hata oluştu");
        }
    }


    @Override
    @Transactional
    public DataResult<UserDTO> activateUser(Long id) {
        logger.info("{} ID'li kullanıcı aktifleştiriliyor", id);
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                if (!user.getStatus().equals("A")) {
                    user.setStatus("A");
                    User savedUser = userRepository.save(user);
                    UserDTO activatedUserDTO = userMapper.toDTO(savedUser);
                    logger.info("{} ID'li kullanıcı başarıyla aktifleştirildi", id);
                    return new SuccessDataResult<>(activatedUserDTO, "Kullanıcı başarıyla aktifleştirildi");
                } else {
                    logger.warn("{} ID'li kullanıcı zaten aktif", id);
                    return new ErrorDataResult<>(null, "Kullanıcı zaten aktif");
                }
            } else {
                logger.warn("{} ID'li kullanıcı bulunamadı", id);
                return new ErrorDataResult<>(null, "Aktifleştirilecek kullanıcı bulunamadı");
            }
        } catch (Exception e) {
            logger.error("{} ID'li kullanıcı aktifleştirilirken bir hata oluştu", id, e);
            return new ErrorDataResult<>(null, "Kullanıcı aktifleştirilirken bir hata oluştu");
        }
    }
}