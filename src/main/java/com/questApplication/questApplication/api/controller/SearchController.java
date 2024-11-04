package com.questApplication.questApplication.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.questApplication.questApplication.repository.elastic.PostElasticsearchRepository;
import com.questApplication.questApplication.entity.elastic.PostDocument;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final PostElasticsearchRepository postElasticsearchRepository;

    public SearchController(PostElasticsearchRepository postElasticsearchRepository) {
        this.postElasticsearchRepository = postElasticsearchRepository;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDocument>> searchPosts(@RequestParam String query) {
        List<PostDocument> results = postElasticsearchRepository
                .findByTitleContainingOrTextContaining(query, query);
        return ResponseEntity.ok(results);
    }
}