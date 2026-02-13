package com.punithan_library.Controller;

import com.punithan_library.Exception.BookException;
import com.punithan_library.Payload.DTO.BookDTO;
import com.punithan_library.Payload.Request.BookSearchRequest;
import com.punithan_library.Payload.Response.ApiResponse;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) throws BookException {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PostMapping ("/bulk")
    public ResponseEntity<?> createBulkBook(@Valid @RequestBody List<BookDTO> bookDTOS) throws BookException {
        List<BookDTO> createdBooks = bookService.createBooksBulk(bookDTOS);
        return ResponseEntity.ok(createdBooks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) throws BookException {
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (BookException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) throws BookException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book deleted sucessfully", true));
    }

    @DeleteMapping("/{id}/permanent")
    public  ResponseEntity<ApiResponse> hardDeleteBook(@PathVariable Long id) throws BookException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book permanently deleted successfully", true));
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> advancedSearchBooks(@RequestBody BookSearchRequest searchRequest) {
        PageResponse<BookDTO> pageResponse = bookService.serachBookWithFilters(searchRequest);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/stats")
    public ResponseEntity<BookStatsResponse> getBookStats(){
        long totalActive = bookService.getTotalActiveBooks();
        long totalAvailable = bookService.getTotalAvailableBooks();

        BookStatsResponse stats = new BookStatsResponse(totalActive, totalAvailable);
        return ResponseEntity.ok(stats);
    }

    /**  Static DTO response   **/
    @Getter @Setter
    public static class BookStatsResponse {
        private long totalActive;
        private long totalAvailable;
        public BookStatsResponse(long totalActive, long totalAvailable){
            this.totalActive = totalActive;
            this.totalAvailable = totalAvailable;
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(@RequestParam(required = false) Long genreId, @RequestParam(defaultValue = "false") Boolean availableOnly,
                                                             @RequestParam(defaultValue = "true") boolean activeOnly, @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size,@RequestParam(defaultValue = "createdAt") String sortBy,
                                                             @RequestParam(defaultValue = "Desc") String sortDirection){
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenreId(genreId);
        searchRequest.setAvailableOnly(availableOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<BookDTO> bookPages = bookService.serachBookWithFilters(searchRequest);
        return ResponseEntity.ok(bookPages);
    }
}
