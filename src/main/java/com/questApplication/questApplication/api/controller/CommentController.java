package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPostId(
            @PathVariable Long postId,
            Pageable pageable) {
        Page<CommentResponseDto> result = commentService.getCommentsByPostId(postId, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user")
    @Operation(summary = "Oturum açmış kullanıcının yorumlarını getir", description = "Oturum açmış kullanıcının yorumlarını sayfalanmış şekilde getirir")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByCurrentUser(
            Pageable pageable,
            Authentication authentication) {
        String username = authentication.getName();
        Page<CommentResponseDto> result = commentService.getCommentsByCurrentUser(username, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Yeni bir yorum oluştur", description = "Yeni bir yorum oluşturur ve oluşturulan yorumu döndürür")
    public ResponseEntity<CommentResponseDto> createComment(
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDto result = commentService.createComment(commentRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Bir yorumu güncelle", description = "Mevcut bir yorumu günceller ve güncellenmiş yorumu döndürür")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            Authentication authentication) {
        String username = authentication.getName();
        CommentResponseDto result = commentService.updateComment(id, commentRequestDto, username);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Bir yorumu sil", description = "Bir yorumu ID'sine göre siler")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        commentService.deleteComment(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre yorum getir", description = "Belirli bir yorumu ID'sine göre getirir")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long id) {
        CommentResponseDto result = commentService.getCommentById(id);
        return ResponseEntity.ok(result);
    }
}
