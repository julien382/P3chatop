package com.openclassrooms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RentalDTO {
    private Long id;
    private String name;
    private Integer surface;
    private Integer price;
    private String picture;
    private String description;
    private Long owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
