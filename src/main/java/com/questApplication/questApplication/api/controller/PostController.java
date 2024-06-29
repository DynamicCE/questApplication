package com.questApplication.questApplication.api.controller;

import com.questApplication.questApplication.business.abstracts.PostService;
import com.questApplication.questApplication.core.utilities.result.DataResult;
import com.questApplication.questApplication.core.utilities.result.Result;
import com.questApplication.questApplication.entity.dto.PostDTO;
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
@RequestMapping("/api/v1/posts")
@Tag(name = "Gönderi Denetleyicisi", description = "Gönderi işlemlerini yönetir")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    @Operation(summary = "Tüm gönderileri getir", description = "Tüm gönderileri sayfalanmış şekilde getirir")
    public ResponseEntity<DataResult<Page<PostDTO>>> getAllPosts(Pageable pageable) {
        logger.debug("getAllPosts isteği alındı. Sayfa: {}, Boyut: {}", pageable.getPageNumber(), pageable.getPageSize());
        DataResult<Page<PostDTO>> result = postService.getAllPosts(pageable);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre gönderi getir", description = "Belirli bir gönderiyi ID'sine göre getirir")
    public ResponseEntity<DataResult<PostDTO>> getPostById(@PathVariable Long id) {
        logger.debug("getPostById isteği alındı. Post ID: {}", id);
        DataResult<PostDTO> result = postService.getPostById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping("/")
    @Operation(summary = "Yeni bir gönderi oluştur", description = "Yeni bir gönderi oluşturur ve oluşturulan gönderiyi döndürür")
    public ResponseEntity<DataResult<PostDTO>> createPost(@Valid @RequestBody PostDTO postDTO) {
        logger.debug("createPost isteği alındı");
        DataResult<PostDTO> result = postService.createPost(postDTO);
        return result.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(result)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Bir gönderiyi güncelle", description = "Mevcut bir gönderiyi günceller ve güncellenmiş gönderiyi döndürür")
    public ResponseEntity<DataResult<PostDTO>> updatePost(@PathVariable Long id, @Valid @RequestBody PostDTO postDTO) {
        logger.debug("updatePost isteği alındı. Post ID: {}", id);
        DataResult<PostDTO> result = postService.updatePost(id, postDTO);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Bir gönderiyi sil", description = "Bir gönderiyi ID'sine göre siler")
    public ResponseEntity<Result> deletePost(@PathVariable Long id) {
        logger.debug("deletePost isteği alındı. Post ID: {}", id);
        Result result = postService.deletePost(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Kullanıcının gönderilerini getir", description = "Belirli bir kullanıcının gönderilerini sayfalanmış şekilde getirir")
    public ResponseEntity<DataResult<Page<PostDTO>>> getPostsByUserId(@PathVariable Long userId, Pageable pageable) {
        logger.debug("getPostsByUserId isteği alındı. User ID: {}, Sayfa: {}, Boyut: {}", userId, pageable.getPageNumber(), pageable.getPageSize());
        DataResult<Page<PostDTO>> result = postService.getPostsByUserId(userId, pageable);
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