package com.punithan_library.Service.Impl;

import com.punithan_library.Entity.BookEntity;
import com.punithan_library.Exception.BookException;
import com.punithan_library.Mapper.BookMapper;
import com.punithan_library.Payload.DTO.BookDTO;
import com.punithan_library.Payload.Request.BookSearchRequest;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Repository.BookRepository;
import com.punithan_library.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;


    @Override
    public BookDTO createBook(BookDTO bookDTO) throws BookException {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())){
            throw new BookException("Book with ISBN already exists");
        }
        BookEntity book = bookMapper.toEntity(bookDTO);
        book.isAvailableCopiesValid();
        BookEntity savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookDTO getBookById(Long bookId) throws BookException {
        BookEntity book =bookRepository.findById(bookId)
                .orElseThrow(()-> new BookException("Book not found"));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDTO> createBooksBulk(List<BookDTO> bookDTOList) throws BookException {

        List<BookDTO> bookDTOS = new ArrayList<>();
       for(BookDTO bookDTO : bookDTOList){
           createBook(bookDTO);
           bookDTOS.add(bookDTO);
       }
       return  bookDTOS;
    }

    @Override
    public BookDTO updateBook(Long bookId,BookDTO bookDTO) throws BookException {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new BookException("Book not found"));
        bookMapper.updateEntityFromDTO(bookDTO, book);
        book.isAvailableCopiesValid();
        BookEntity savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookDTO getBookByISBN(String ISBN) throws BookException {
        BookEntity book =bookRepository.findByIsbn(ISBN)
                .orElseThrow(()-> new BookException("Book not found"));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBook(Long bookId) throws BookException {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new BookException("Book not found"));
        book.setActive(false);
        bookRepository.save(book);
    }

    @Override
    public void hardDeleteBook(Long bookId) throws BookException {
        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(()-> new BookException("Book not found"));
        bookRepository.delete(book);
    }

    @Override
    public PageResponse<BookDTO> serachBookWithFilters(BookSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest.getPage(), searchRequest.getSize(), searchRequest.getSortBy(), searchRequest.getSortDirection());
        Page<BookEntity> bookPage = bookRepository.searchBooksWithFilters(
                searchRequest.getSearchTerm(), searchRequest.getGenreId(), searchRequest.getAvailableOnly(), pageable
        );
        return convertToPageResponse(bookPage);
    }

    @Override
    public Long getTotalActiveBooks() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public Long getTotalAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        size = Math.min(size, 10);
        size = Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")?
                Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    private PageResponse<BookDTO> convertToPageResponse(Page<BookEntity> books){
        List<BookDTO> bookDTOS = books.getContent()
                .stream().map(bookMapper::toDto).collect(Collectors.toList());

        return new PageResponse<>(bookDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty());
    }
}
