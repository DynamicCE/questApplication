package com.questApplication.questApplication.entity.elastic;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.Data;

@Data
@Document(indexName = "posts")
public class PostDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "turkish")
    private String title;

    @Field(type = FieldType.Text, analyzer = "turkish")
    private String text;

    @Field(type = FieldType.Keyword)
    private String username;

    @Field(type = FieldType.Long)
    private Long likeCount;
}