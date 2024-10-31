package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.core.utilities.exception.UnauthorizedException;
import com.questApplication.questApplication.core.utilities.jwt.JwtUtil;
import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.AuthResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.questApplication.questApplication.business.abstracts.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.redisTemplate = redisTemplate;
    }

    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yaparak JWT token döner")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        AuthResponseDto tokens = authService.login(
                userRequestDto.getUsername(),
                userRequestDto.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Yeni bir kullanıcı oluşturur")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        authService.register(userRequestDto);
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi");
    }

    @Operation(summary = "Kullanıcı çıkışı", description = "Kullanıcı çıkış yaparak JWT token'ı sildirir")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization") String token) {
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
        }

        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @RequestHeader("Authorization") String refreshToken) {

        refreshToken = refreshToken.substring(7);

        String username = jwtUtil.validateTokenAndGetUsername(refreshToken);
        String storedToken = redisTemplate.opsForValue()
                .get("refresh_token:" + username);

        if (!refreshToken.equals(storedToken)) {
            throw new UnauthorizedException("Geçersiz refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username);

        return ResponseEntity.ok(AuthResponseDto.of(
                newAccessToken,
                refreshToken, // aynı refresh token
                jwtUtil.getAccessTokenExpiration()));
    }
}
