package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.entity.dto.request.PostRequestDto;
import com.questApplication.questApplication.entity.dto.response.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Gönderi Denetleyicisi", description = "Gönderi işlemlerini yönetir")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Tüm gönderileri getir", description = "Tüm gönderileri sayfalanmış şekilde getirir")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        Page<PostResponseDto> result = postService.getAllPosts(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre gönderi getir", description = "Belirli bir gönderiyi ID'sine göre getirir")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Yeni bir gönderi oluştur", description = "Yeni bir gönderi oluşturur ve oluşturulan gönderiyi döndürür")
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto, Authentication authentication) {
        String username = authentication.getName();
        PostResponseDto result = postService.createPost(postRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Bir gönderiyi güncelle", description = "Mevcut bir gönderiyi günceller ve güncellenmiş gönderiyi döndürür")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequestDto postRequestDto, Authentication authentication) {
        String username = authentication.getName();
        PostResponseDto result = postService.updatePost(id, postRequestDto, username);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Bir gönderiyi sil", description = "Bir gönderiyi ID'sine göre siler")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        postService.deletePost(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    @Operation(summary = "Kullanıcının gönderilerini getir", description = "Oturum açmış kullanıcının gönderilerini sayfalanmış şekilde getirir")
    public ResponseEntity<Page<PostResponseDto>> getPostsByUser(Pageable pageable, Authentication authentication) {
        String username = authentication.getName();
        Page<PostResponseDto> result = postService.getPostsByUser(username, pageable);
        return ResponseEntity.ok(result);
    }
}
