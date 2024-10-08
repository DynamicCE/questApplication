package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.UserService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Kullanıcı Denetleyicisi", description = "Kullanıcı işlemlerini yönetir")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    @Operation(summary = "Tüm kullanıcıları getir", description = "Tüm kullanıcıları sayfalanmış şekilde getirir")
    public ResponseEntity<DataResult<Page<UserDTO>>> getAllUsers(Pageable pageable) {
        DataResult<Page<UserDTO>> result = userService.getAllUsers(pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kullanıcı getir", description = "Belirli bir kullanıcıyı ID'sine göre getirir")
    public ResponseEntity<DataResult<UserDTO>> getUserById(@PathVariable Long id) {
        DataResult<UserDTO> result = userService.getUserById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping("/")
    @Operation(summary = "Yeni bir kullanıcı oluştur", description = "Yeni bir kullanıcı oluşturur ve oluşturulan kullanıcıyı döndürür")
    public ResponseEntity<DataResult<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        DataResult<UserDTO> result = userService.createUser(userDTO);
        return result.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(result)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Bir kullanıcıyı güncelle", description = "Mevcut bir kullanıcıyı günceller ve güncellenmiş kullanıcıyı döndürür")
    public ResponseEntity<DataResult<UserDTO>> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        DataResult<UserDTO> result = userService.updateUser(id, userDTO);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Bir kullanıcıyı sil", description = "Bir kullanıcıyı ID'sine göre siler")
    public ResponseEntity<Result> deleteUser(@PathVariable Long id) {
        Result result = userService.deleteUser(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu");
    }
}
