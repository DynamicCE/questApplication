package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.CommentDTO;
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
//test
@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "Yorum Denetleyicisi", description = "Yorum işlemlerini yönetir")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Gönderi ID'sine göre yorumları getir", description = "Belirli bir gönderiye ait sayfalanmış yorumları getirir")
    public ResponseEntity<DataResult<Page<CommentDTO>>> getCommentsByPostId(
            @PathVariable Long postId,
            Pageable pageable) {
        logger.debug("getCommentsByPostId isteği alındı. Post ID: {}, Sayfa: {}, Boyut: {}", postId, pageable.getPageNumber(), pageable.getPageSize());
        DataResult<Page<CommentDTO>> result = commentService.getCommentsByPostId(postId, pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Kullanıcı ID'sine göre yorumları getir", description = "Belirli bir kullanıcıya ait sayfalanmış yorumları getirir")
    public ResponseEntity<DataResult<Page<CommentDTO>>> getCommentsByUserId(
            @PathVariable Long userId,
            Pageable pageable) {
        logger.debug("getCommentsByUserId isteği alındı. User ID: {}, Sayfa: {}, Boyut: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        DataResult<Page<CommentDTO>> result = commentService.getCommentsByUserId(userId, pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    @Operation(summary = "Yeni bir yorum oluştur", description = "Yeni bir yorum oluşturur ve oluşturulan yorumu döndürür")
    public ResponseEntity<DataResult<CommentDTO>> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        logger.debug("createComment isteği alındı");
        DataResult<CommentDTO> result = commentService.createComment(commentDTO);
        return result.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(result)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Bir yorumu güncelle", description = "Mevcut bir yorumu günceller ve güncellenmiş yorumu döndürür")
    public ResponseEntity<DataResult<CommentDTO>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentDTO commentDTO) {
        logger.debug("updateComment isteği alındı. Comment ID: {}", id);
        DataResult<CommentDTO> result = commentService.updateComment(id, commentDTO);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Bir yorumu sil", description = "Bir yorumu ID'sine göre siler")
    public ResponseEntity<Result> deleteComment(@PathVariable Long id) {
        logger.debug("deleteComment isteği alındı. Comment ID: {}", id);
        Result result = commentService.deleteComment(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre yorum getir", description = "Belirli bir yorumu ID'sine göre getirir")
    public ResponseEntity<DataResult<CommentDTO>> getCommentById(@PathVariable Long id) {
        logger.debug("getCommentById isteği alındı. Comment ID: {}", id);
        DataResult<CommentDTO> result = commentService.getCommentById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Beklenmeyen bir hata oluştu: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu");
    }
}