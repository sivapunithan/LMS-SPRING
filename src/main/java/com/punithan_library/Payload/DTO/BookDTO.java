package com.punithan_library.Payload.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter @Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "ISBN is Mandatory")
    private String isbn;

    @NotBlank(message = "Title is Mandatory")
    private String title;

    @NotBlank(message = "Author is Mandatory")
    private String author;

    @NotNull(message = "Genre is Mandatory")
    private Long genreId;

    private String genreName;

    private String genreCode;

    @Size(max = 100, message = "Publisher name must not exceed 100 characters")
    private String publisherName;

    private LocalDate publicationDate;

    @Size(max = 20, message = "Language must not exceed 20 characters")
    private String language;

    @Min(value = 1, message = "Pages must be atleast 1")
    @Max(value = 50000, message = "Pages must not exceed 50000")
    private Integer pages;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Min(value = 0, message = "Total copies cannot be negative")
    private Integer totalCopies;

    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal price;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String coverImageUrl;

    private Boolean alreadyHaveLoan;

    private Boolean alreadyHaveReservation;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
