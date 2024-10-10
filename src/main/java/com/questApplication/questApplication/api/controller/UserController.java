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

    @GetMapping("/{id}")
    @Operation(summary = "Id'ye göre kullanıcı bulma")
    public
    ResponseEntity<UserResponseDto> getUserById ( @PathVariable Long id ) {
        UserResponseDto result = userService.getUserById ( id );
        return ResponseEntity.ok ( result );
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Kullanıcı adına göre aratma")
    public
    ResponseEntity<UserResponseDto> getUserByUsername ( String username ) {
        UserResponseDto result = userService.getUserByUsername ( username );
        return ResponseEntity.ok ( result );
    }

    @PostMapping()
    @Operation(summary = "Profil oluşturma")
    public
    ResponseEntity<Void> createUser ( @Valid @RequestBody UserRequestDto userRequestDto ) {
        userService.createUser ( userRequestDto );
        return ResponseEntity.status ( HttpStatus.CREATED ).build ( );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Profil Güncelleme")
    public
    ResponseEntity<Void> updateUser ( Long id, UserRequestDto UserRequestDto ) {
        userService.updateUser ( id, UserRequestDto );
        return ResponseEntity.ok ( ).build ( );
    }

    @PostMapping("/{id}")
    @Operation(summary = "Profili Sil")
    public
    ResponseEntity<Void> deleteUser ( @PathVariable Long id, Authentication authentication ) {
        String username = authentication.getName ( );
        userService.deleteUser ( id,username );
        return ResponseEntity.noContent ( ).build ( );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Profili Aktive Et")
    public
    ResponseEntity<Void> activateUser ( @PathVariable Long id, Authentication authentication ) {
        String username = authentication.getName ( );
        userService.activateUser ( id );
        return ResponseEntity.noContent ( ).build ( );
    }

}
