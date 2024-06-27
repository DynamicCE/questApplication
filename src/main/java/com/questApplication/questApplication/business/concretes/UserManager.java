package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.results.*;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.UserDTO;

import com.questApplication.questApplication.mapper.UserMapper;
import com.questApplication.questApplication.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManager implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserManager(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public DataResult<List<UserDTO>> getAllUsers() {
        logger.info("Tüm kullanıcılar getiriliyor");
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOs = users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Toplam {} kullanıcı başarıyla getirildi", userDTOs.size());
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
            User user = userRepository.findById(id).orElse(null);
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
    public DataResult<UserDTO> createUser(UserDTO userDTO) {
        logger.info("Yeni kullanıcı oluşturuluyor");
        try {
            User user = userMapper.toEntity(userDTO);
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
    public DataResult<UserDTO> updateUser(Long id, UserDTO userDTO) {
        logger.info("{} ID'li kullanıcı güncelleniyor", id);
        try {
            User existingUser = userRepository.findById(id).orElse(null);
            if (existingUser != null) {
                User updatedUser = userMapper.toEntity(userDTO);
                updatedUser.setId(id);
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
    public Result deleteUser(Long id) {
        logger.info("{} ID'li kullanıcı siliniyor", id);
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                logger.info("{} ID'li kullanıcı başarıyla silindi", id);
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
}