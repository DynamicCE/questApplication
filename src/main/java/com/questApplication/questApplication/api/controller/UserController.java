package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.entity.dto.request.UserRequestDto;
import com.questApplication.questApplication.entity.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Kullanıcı Denetleyicisi", description = "Kullanıcı işlemlerini yönetir")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tüm kullanıcıları getir", description = "Tüm kullanıcıları sayfalanmış şekilde getirir (Sadece ADMIN)")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    @Operation(summary = "Mevcut kullanıcı bilgilerini getir", description = "Oturum açmış kullanıcının bilgilerini getirir")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    @Operation(summary = "Mevcut kullanıcıyı güncelle", description = "Oturum açmış kullanıcının bilgilerini günceller")
    public ResponseEntity<Void> updateCurrentUser(@Valid @RequestBody UserRequestDto userRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUser(null, userRequestDto, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @Operation(summary = "Mevcut kullanıcıyı sil", description = "Oturum açmış kullanıcının hesabını siler")
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(null, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "ID'ye göre kullanıcı getir", description = "Belirli bir kullanıcıyı ID'sine göre getirir (Sadece ADMIN)")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcıyı aktifleştir", description = "Belirli bir kullanıcıyı aktifleştirir (Sadece ADMIN)")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id, null);
        return ResponseEntity.noContent().build();
    }
}
