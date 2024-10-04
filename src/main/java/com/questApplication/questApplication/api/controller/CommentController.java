package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.CommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "Yorum Denetleyicisi", description = "Yorum işlemlerini yönetir")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Gönderi ID'sine göre yorumları getir", description = "Belirli bir gönderiye ait sayfalanmış yorumları getirir")
    public ResponseEntity<DataResult<Page<CommentDTO>>> getCommentsByPostId(
            @PathVariable Long postId,
            Pageable pageable) {
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
        DataResult<Page<CommentDTO>> result = commentService.getCommentsByUserId(userId, pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    @Operation(summary = "Yeni bir yorum oluştur", description = "Yeni bir yorum oluşturur ve oluşturulan yorumu döndürür")
    public ResponseEntity<DataResult<CommentDTO>> createComment(@Valid @RequestBody CommentDTO commentDTO) {
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
        DataResult<CommentDTO> result = commentService.updateComment(id, commentDTO);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Bir yorumu sil", description = "Bir yorumu ID'sine göre siler")
    public ResponseEntity<Result> deleteComment(@PathVariable Long id) {
        Result result = commentService.deleteComment(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre yorum getir", description = "Belirli bir yorumu ID'sine göre getirir")
    public ResponseEntity<DataResult<CommentDTO>> getCommentById(@PathVariable Long id) {
        DataResult<CommentDTO> result = commentService.getCommentById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Beklenmeyen bir hata oluştu");
    }
}
