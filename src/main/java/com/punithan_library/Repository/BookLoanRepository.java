package com.punithan_library.Repository;

import com.punithan_library.Domain.BookLoanStatus;
import com.punithan_library.Entity.BookLoanEntity;
import com.punithan_library.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookLoanRepository extends JpaRepository<BookLoanEntity, Long> {

    Page<BookLoanEntity> findByStatus(BookLoanStatus status, Pageable pageable);

    Page<BookLoanEntity> findByUserId(Long userId, Pageable pageable);

    Page<BookLoanEntity> findByStatusAndUser(BookLoanStatus status, UserEntity user, Pageable pageable);

    Page<BookLoanEntity> findByBookId(Long bookId, Pageable pageable);

    @Query("SELECT case WHEN count(bl) > 0 then true else false end from BookLoanEntity bl " +
            "WHERE bl.user.id =:userId and bl.book.id =:bookId " +
            "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')")
    Boolean hasActiveCheckOut(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT COUNT(bl) FROM BookLoanEntity bl WHERE bl.user.id =:userId " +
            "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE' )")
    Long countActiveBookLoansByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(bl) FROM BookLoanEntity bl WHERE bl.user.id =:userId " +
            "AND  bl.status = 'OVERDUE' ")
    Long countOverDueBookLoanByUser(Long userId);

    @Query("SELECT bl from BookLoanEntity bl WHERE bl.dueDate <:currentDate " +
            "AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE' )")
    Page<BookLoanEntity> findOverDueBookLoans(@Param("currentDate")LocalDate currentDate, Pageable pageable);

    @Query("SELECT bl from BookLoanEntity bl WHERE bl.checkOutDate BETWEEN :startDate AND :endDate ")
    Page<BookLoanEntity> findBookLoanByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
