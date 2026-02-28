package com.punithan_library.Repository;

import com.punithan_library.Domain.FineStatus;
import com.punithan_library.Domain.FineType;
import com.punithan_library.Entity.FineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    @Query("""
        SELECT f FROM FineEntity f
        WHERE (:userId IS NULL OR f.user.id = :userId)
        AND (:status IS NULL OR f.status = :status)
        AND (:fineType IS NULL OR f.type = :fineType)
        ORDER BY f.createdAt DESC
        """)
    Page<FineEntity> findAllWithFilters(@Param("userId") Long userId, @Param("status") FineStatus status, @Param("fineType") FineType fineType, Pageable pageable);

    List<FineEntity> findByUserId(Long userId);

    List<FineEntity> findByUserIdAndType(Long userId, FineType type);
}
