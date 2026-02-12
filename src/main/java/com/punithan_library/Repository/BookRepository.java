package com.punithan_library.Repository;

import com.punithan_library.Entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findByIsbn(String ISBN);

    boolean existsByIsbn(String ISBN);

    @Query("""
                SELECT b FROM BookEntity b
                WHERE b.active = true
                AND (
                    :searchTerm IS NULL 
                    OR LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                )
                AND (
                    :genreId IS NULL 
                    OR b.genre.id = :genreId
                )
                AND (
                    :availableOnly = false 
                    OR b.availableCopies > 0
                )
            """)
    Page<BookEntity> searchBooksWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("genreId") Long genreId,
            @Param("availableOnly") Boolean availableOnly,
            Pageable pageable
    );

    long countByActiveTrue();

    @Query("SELECT COUNT(b) FROM BookEntity b WHERE b.active = true AND b.availableCopies > 0")
    long countAvailableBooks();

}
