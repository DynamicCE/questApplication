package com.questApplication.questApplication.business.abstracts;

import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.AuthResponseDto;

public interface AuthService {
    public void register(UserRequestDto userRequestDto);

    public AuthResponseDto login(String Username, String password);

    public void logout(String token);
}
