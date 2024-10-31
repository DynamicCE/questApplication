package com.questApplication.questApplication.business.concretes;

import com.questApplication.questApplication.business.abstracts.AuthService;
import com.questApplication.questApplication.core.utilities.exception.ResourceNotFoundException;
import com.questApplication.questApplication.core.utilities.exception.UnauthorizedException;
import com.questApplication.questApplication.core.utilities.jwt.JwtUtil;
import com.questApplication.questApplication.entity.User;
import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.AuthResponseDto;
import com.questApplication.questApplication.entity.enums.UserRole;
import com.questApplication.questApplication.mapper.UserMapper;
import com.questApplication.questApplication.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;

@Service
public class AuthManager implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthManager(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void register(UserRequestDto userRequestDto) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new IllegalArgumentException("Kullanıcı adı zaten alınmış");
        }

        User user = userMapper.toEntity(userRequestDto);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setStatus("A");
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Kullanıcı adı veya şifre hatalı");
        }

        if (!userRepository.existsByUsernameAndStatusNot(username, "D")) {
            throw new ResourceNotFoundException("Kullanıcı bulunamadı");
        }

        String accessToken = jwtUtil.generateAccessToken(username);

        String refreshToken = jwtUtil.generateRefreshToken(username);

        redisTemplate.opsForValue().set(
                "refresh_token:" + username,
                refreshToken,
                jwtUtil.getRefreshTokenExpiration(),
                TimeUnit.MILLISECONDS);

        return AuthResponseDto.of(
                accessToken,
                refreshToken,
                jwtUtil.getAccessTokenExpiration());
    }

    @Override
    public void logout(String token) {
        return;
    }

}
