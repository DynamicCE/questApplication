package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Kullanıcı Denetleyicisi", description = "Kullanıcı işlemlerini yönetir")
public
class UserController {

    private final UserService userService;

    public
    UserController ( UserService userService ) {
        this.userService = userService;
    }



}
