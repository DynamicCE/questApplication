package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.entity.dto.request.LikeRequestDto;
import com.questApplication.questApplication.entity.dto.response.LikeResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@Tag(name = "Beğeni Denetleyicisi", description = "Beğeni işlemlerini yönetir")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    @Operation(summary = "Tüm beğenileri getir", description = "Tüm beğenileri sayfalanmış şekilde getirir")
    public ResponseEntity<Page<LikeResponseDto>> getAllLikes(Pageable pageable) {
        Page<LikeResponseDto> result = likeService.getAllLikes(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Gönderi ID'sine göre beğenileri getir", description = "Belirli bir gönderiye ait sayfalanmış beğenileri getirir")
    public ResponseEntity<Page<LikeResponseDto>> getLikesByPostId(
            @PathVariable Long postId,
            Pageable pageable) {
        Page<LikeResponseDto> result = likeService.getLikesByPostId(postId, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Yeni bir beğeni oluştur", description = "Yeni bir beğeni oluşturur ve sonucu döndürür")
    public ResponseEntity<LikeResponseDto> createLike(@RequestBody LikeRequestDto likeRequestDto, Authentication authentication) {
        String username = authentication.getName();
        LikeResponseDto result = likeService.createLike(likeRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/count/{postId}")
    @Operation(summary = "Gönderi beğeni sayısını getir", description = "Belirli bir gönderinin toplam beğeni sayısını getirir")
    public ResponseEntity<Long> getLikeCountForPost(@PathVariable Long postId) {
        long result = likeService.getLikeCountForPost(postId);
        return ResponseEntity.ok(result);
    }
}
