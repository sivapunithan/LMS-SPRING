package com.punithan_library.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Genre Code is mandatory")
    private String genreCode;
    @NotNull(message = "Genre name is required")
    private String genreName;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    @Min(value = 0, message ="Display order cannot be active")
    private Integer displayOrder;
    @Column(nullable = false)
    private Boolean active;
    @ManyToOne
    private GenreEntity parentGenre;

    @OneToMany
    private List<GenreEntity> subGenres = new ArrayList<>();
//    @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST)
////    private List<Book>  books = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime creationAt;
    @UpdateTimestamp
    private LocalDateTime updateAt;

}
