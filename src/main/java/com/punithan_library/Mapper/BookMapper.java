package com.punithan_library.Mapper;

import com.punithan_library.Entity.BookEntity;
import com.punithan_library.Entity.GenreEntity;
import com.punithan_library.Exception.BookException;
import com.punithan_library.Payload.DTO.BookDTO;
import com.punithan_library.Repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    @Autowired
    private GenreRepository genreRepository;

    public BookDTO toDto(BookEntity book){
        if (book == null) return null;
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .genreId(book.getGenre().getId())
                .genreName(book.getGenre().getGenreName())
                .genreCode(book.getGenre().getGenreCode())
                .publisherName(book.getPublisher())
                .publicationDate(book.getPublishedDate())
                .language(book.getLanguage())
                .pages(book.getPages())
                .description(book.getDescription())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .price(book.getPrice())
                .coverImageUrl(book.getCoverImageUrl())
                .active(book.getActive())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
    public BookEntity toEntity(BookDTO bookDTO) throws BookException {
        if (bookDTO == null) return null;

        BookEntity book = new BookEntity();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setIsbn(bookDTO.getIsbn());
        book.setAuthor(bookDTO.getAuthor());

        if (bookDTO.getGenreId() != null){
            GenreEntity genre = genreRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(()-> new BookException ("Genre not found"));
            book.setGenre(genre);
        }
        book.setPublisher(bookDTO.getPublisherName());
        book.setPublishedDate(bookDTO.getPublicationDate());
        book.setLanguage(bookDTO.getLanguage());
        book.setPages(bookDTO.getPages());
        book.setDescription(bookDTO.getDescription());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setPrice(bookDTO.getPrice());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        book.setActive(bookDTO.getActive());

        return book;

    }

    public void updateEntityFromDTO(BookDTO bookDTO, BookEntity book) throws BookException{
        if (book == null || bookDTO == null) return;

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());

        if (bookDTO.getGenreId() != null){
            GenreEntity genre = genreRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(()-> new BookException("Genre not found"));
            book.setGenre(genre);
        }
        book.setPublisher(bookDTO.getPublisherName());
        book.setPublishedDate(bookDTO.getPublicationDate());
        book.setLanguage(bookDTO.getLanguage());
        book.setPages(bookDTO.getPages());
        book.setDescription(bookDTO.getDescription());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setPrice(bookDTO.getPrice());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        book.setActive(bookDTO.getActive());
    }
}
