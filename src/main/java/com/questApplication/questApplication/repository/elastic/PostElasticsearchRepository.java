package com.questApplication.questApplication.repository.elastic;

import com.questApplication.questApplication.entity.elastic.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface PostElasticsearchRepository extends ElasticsearchRepository<PostDocument, String> {
    List<PostDocument> findByTitleContainingOrTextContaining(String title, String text);
}