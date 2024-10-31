package com.questApplication.questApplication.entity.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // Setter otomatik
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;

    public static AuthResponseDto of(String accessToken, String refreshToken, long expiresIn) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .build();
    }
}