package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/likes")
@Tag(name = "Beğeni Denetleyicisi", description = "Beğeni işlemlerini yönetir")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/")
    @Operation(summary = "Tüm beğenileri getir", description = "Tüm beğenileri sayfalanmış şekilde getirir")
    public ResponseEntity<DataResult<Page<LikeDTO>>> getAllLikes(Pageable pageable) {
        DataResult<Page<LikeDTO>> result = likeService.getAllLikes(pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Gönderi ID'sine göre beğenileri getir", description = "Belirli bir gönderiye ait sayfalanmış beğenileri getirir")
    public ResponseEntity<DataResult<Page<LikeDTO>>> getLikesByPostId(
            @PathVariable Long postId,
            Pageable pageable) {
        DataResult<Page<LikeDTO>> result = likeService.getLikesByPostId(postId, pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping("/")
    @Operation(summary = "Yeni bir beğeni oluştur", description = "Yeni bir beğeni oluşturur ve sonucu döndürür")
    public ResponseEntity<Result> createLike(@Valid @RequestBody LikeDTO likeDTO) {
        Result result = likeService.createLike(likeDTO);
        return result.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(result)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping("/count/{postId}")
    @Operation(summary = "Gönderi beğeni sayısını getir", description = "Belirli bir gönderinin toplam beğeni sayısını getirir")
    public ResponseEntity<DataResult<Long>> getLikeCountForPost(@PathVariable Long postId) {
        DataResult<Long> result = likeService.getLikeCountForPost(postId);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu");
    }
}
