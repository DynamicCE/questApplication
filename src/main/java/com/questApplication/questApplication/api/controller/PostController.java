package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.entity.dto.request.CommentRequestDto;
import com.questApplication.questApplication.entity.dto.request.PostRequestDto;
import com.questApplication.questApplication.entity.dto.response.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.questApplication.questApplication.business.abstracts.CommentService;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Gönderi Denetleyicisi", description = "Gönderi işlemlerini yönetir")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    @Operation(summary = "Tüm gönderileri getir", description = "Tüm gönderileri sayfalanmış şekilde getirir")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        Page<PostResponseDto> result = postService.getAllPosts(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "ID'ye göre gönderi getir", description = "Belirli bir gönderiyi ID'sine göre getirir")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Yeni bir gönderi oluştur", description = "Yeni bir gönderi oluşturur ve oluşturulan gönderiyi döndürür")
    public ResponseEntity<Long> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Long postId = postService.createPost(postRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Bir gönderiyi güncelle", description = "Mevcut bir gönderiyi günceller ve güncellenmiş gönderiyi döndürür")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        postService.updatePost(id, postRequestDto, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Bir gönderiyi sil", description = "Bir gönderiyi ID'sine göre siler")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        postService.deletePost(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    @Operation(summary = "Kullanıcının gönderilerini getir", description = "Oturum açmış kullanıcının gönderilerini sayfalanmış şekilde getirir")
    public ResponseEntity<Page<PostResponseDto>> getPostsByUser(Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Page<PostResponseDto> result = postService.getPostsByUser(username, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{postId}/comments")
    @Operation(summary = "Bir gönderiye yorum yap")
    public ResponseEntity<Void> addCommentToPost(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        commentService.createComment(commentRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
