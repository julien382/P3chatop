package com.openclassrooms.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class RentalRequestDTO {

    private Long id;

    @Length(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Range(min = 1, message = "Surface must be greater than 0")
    private Integer surface;

    @Range(min = 1, message = "Price must be greater than 0")
    private Integer price;

    private MultipartFile picture;

    @Length(min = 10, message = "Description must be at least 10 characters")
    private String description;

    private Long owner_id;

}