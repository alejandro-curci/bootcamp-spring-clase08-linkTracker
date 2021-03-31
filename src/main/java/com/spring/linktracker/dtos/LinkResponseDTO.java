package com.spring.linktracker.dtos;

import lombok.Data;

@Data
public class LinkResponseDTO {
    private String url;
    private int id;
    private int redirectCount;
    private String password;

    public LinkResponseDTO() {
        redirectCount = 0;
    }
}
