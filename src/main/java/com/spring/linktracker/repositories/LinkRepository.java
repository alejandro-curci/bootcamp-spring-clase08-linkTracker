package com.spring.linktracker.repositories;

import com.spring.linktracker.dtos.LinkResponseDTO;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Data
public class LinkRepository {
    private Map<Integer, LinkResponseDTO> database;
    private int countID;

    public LinkRepository() {
        database = new HashMap<>();
        countID = 0;
    }
}
