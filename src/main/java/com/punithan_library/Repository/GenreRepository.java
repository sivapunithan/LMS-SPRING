package com.punithan_library.Repository;

import com.punithan_library.Entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    List<GenreEntity> findByActiveTrueOrderByDisplayOrderAsc();

    List<GenreEntity> findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

    List<GenreEntity> findByParentGenreIdAndActiveTrueOrderByDisplayOrderAsc(long parentGenreId);

    long countByActiveTrue();

//    @Query("SELECT count(b) from book b where b.genreId=:genreId")
//    long countBooksByGenre(@Param("genreId") long genreId);
}
