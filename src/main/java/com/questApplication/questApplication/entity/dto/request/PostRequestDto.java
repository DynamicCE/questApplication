package com.questApplication.questApplication.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostRequestDto {

    @NotNull(message = "Başlık boş olamaz")
    @Size(min = 1, max = 100, message = "Başlık 1 ile 100 karakter arasında olmalıdır")
    private String title;

    @NotNull(message = "İçerik boş olamaz")
    private String text;

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
}
