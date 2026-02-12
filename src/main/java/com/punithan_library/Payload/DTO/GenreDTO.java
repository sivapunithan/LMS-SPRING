package com.punithan_library.Payload.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor @Builder
public class GenreDTO {

    private Long id;
    @NotNull(message = "Genre Code is mandatory")
    private String genreCode;
    @NotNull(message = "Genre name is required")
    private String genreName;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    @Min(value = 0, message ="Display order cannot be active")
    private Integer displayOrder;
    private Boolean active;
    private Long parentGenreId;
    private String parentGenreName;
    private List<GenreDTO>subGenre;
    private Long bookCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
