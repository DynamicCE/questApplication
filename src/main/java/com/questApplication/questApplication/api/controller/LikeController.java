package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.LikeService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.LikeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Gönderi ID'sine göre beğenileri getir", description = "Belirli bir gönderiye ait sayfalanmış beğenileri getirir")
    public ResponseEntity<DataResult<Page<LikeDTO>>> getLikesByPostId(
            @PathVariable Long postId,
            Pageable pageable) {
        logger.debug("Gönderi ID'si {} için beğeniler getiriliyor, Sayfa: {}, Boyut: {}", postId, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(likeService.getLikesByPostId(postId, pageable));
    }

    @PostMapping
    @Operation(summary = "Yeni bir beğeni oluştur", description = "Yeni bir beğeni oluşturur ve sonucu döndürür")
    public ResponseEntity<Result> createLike(@Valid @RequestBody LikeDTO likeDTO) {
        logger.info("Yeni beğeni oluşturuluyor");
        Result result = likeService.createLike(likeDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(result);
    }

    @GetMapping("/count/{postId}")
    @Operation(summary = "Gönderi beğeni sayısını getir", description = "Belirli bir gönderinin toplam beğeni sayısını getirir")
    public ResponseEntity<DataResult<Long>> getLikeCountForPost(@PathVariable Long postId) {
        logger.debug("Gönderi ID'si {} için beğeni sayısı getiriliyor", postId);
        return ResponseEntity.ok(likeService.getLikeCountForPost(postId));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Beklenmeyen bir hata oluştu: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu");
    }
}