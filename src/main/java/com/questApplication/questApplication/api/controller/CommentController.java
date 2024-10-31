package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.CommentService;
import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.response.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Post yorumlarını getirir")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPostId(@PathVariable Long id, Pageable pageable) {
        Page<CommentResponseDto> comments = commentService.getCommentsByPostId(id, pageable);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Yorumlara yapılan yorumları getirir")
    @GetMapping("/{commentId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByCommentId(@PathVariable Long id, Pageable pageable) {
        Page<CommentResponseDto> comments = commentService.getCommentsByCommentId(id, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @Operation(summary = "Yorum oluşturur")
    public ResponseEntity<Void> CreateComment(@RequestBody CommentRequestDto commentRequestDto, String username) {
        commentService.createComment(commentRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}")
    @Operation(summary = "Yorum Günceller")
    public ResponseEntity<Void> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto,
            String username) {
        commentService.updateComment(id, commentRequestDto, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Yorumu Siler")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, String username) {
        commentService.deleteComment(id, username);
        return ResponseEntity.noContent().build();
    }
}