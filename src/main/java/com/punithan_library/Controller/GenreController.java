package com.punithan_library.Controller;

import com.punithan_library.Exception.GenreException;
import com.punithan_library.Payload.DTO.GenreDTO;
import com.punithan_library.Payload.Response.ApiResponse;
import com.punithan_library.Service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/genres")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @PostMapping("/create")
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreEntity){
        GenreDTO genre = genreService.createGenre(genreEntity);
        return ResponseEntity.ok(genre);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGenres(){
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<?> getGenreById(@PathVariable long genreId) throws GenreException {
        GenreDTO genreDTO = genreService.getGenreById(genreId);
        return ResponseEntity.ok(genreDTO);
    }

    @PutMapping("/{genreId}")
    public  ResponseEntity<?> updateGenre(@PathVariable long genreId, @RequestBody GenreDTO genreDTO) throws GenreException {
        GenreDTO genre = genreService.updateGenre(genreId, genreDTO);
        return ResponseEntity.ok(genre);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable long genreId) throws GenreException {
        genreService.deleteGenre(genreId);
        ApiResponse response = new ApiResponse("Genre deleted - soft delete", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{genreId}/hard")
    public ResponseEntity<?> deleteHardGenre(@PathVariable long genreId) throws GenreException {
        genreService.deleteGenre(genreId);
        ApiResponse response = new ApiResponse("Genre deleted - hard delete", true);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/top-level")
    public ResponseEntity<?> getToplevelGenres(){
        List<GenreDTO> genres = genreService.getTopLevelGenres();
        return  ResponseEntity.ok(genres);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getTotalActiveGenres(){
        Long count = genreService.getTotalActiveGenres();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<?> getBookCount(@PathVariable long id){
        Long count = genreService.getBookCountByGenre(id);
        return  ResponseEntity.ok(count);
    }
}
