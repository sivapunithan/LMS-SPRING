package com.punithan_library.Service;

import com.punithan_library.Exception.GenreException;
import com.punithan_library.Payload.DTO.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreService {

    GenreDTO createGenre(GenreDTO genreEntity);

    List<GenreDTO> getAllGenres();

    GenreDTO getGenreById(Long genreId) throws GenreException;

    GenreDTO updateGenre(Long genreId, GenreDTO genreDto) throws GenreException;

    void deleteGenre(Long genreId) throws GenreException;

    void hardDelete(Long genreId) throws GenreException;

    List<GenreDTO> getAllActiveGenreWithSubGenres();

    List<GenreDTO> getTopLevelGenres();

   // Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable);

    long getTotalActiveGenres();

    long getBookCountByGenre(Long genreId);
}
