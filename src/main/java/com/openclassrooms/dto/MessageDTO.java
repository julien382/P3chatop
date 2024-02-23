package com.openclassrooms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private Long rental_id;
    private Long user_id;
    private String message;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
