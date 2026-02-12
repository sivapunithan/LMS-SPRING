package com.punithan_library.Service;


import com.punithan_library.Exception.BookException;
import com.punithan_library.Payload.DTO.BookDTO;
import com.punithan_library.Payload.Request.BookSearchRequest;
import com.punithan_library.Payload.Response.PageResponse;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO) throws BookException;

    BookDTO getBookById(Long bookId) throws BookException;

    List<BookDTO> createBooksBulk(List<BookDTO> bookDTOList) throws BookException;

    BookDTO updateBook(Long bookId,BookDTO bookDTO) throws BookException;

    BookDTO getBookByISBN(String ISBN) throws BookException;

    void deleteBook(Long bookId) throws BookException;

    void hardDeleteBook(Long bookId) throws BookException;

    PageResponse<BookDTO> serachBookWithFilters(
            BookSearchRequest searchRequest
    );

    Long getTotalActiveBooks();

    Long getTotalAvailableBooks();
}
