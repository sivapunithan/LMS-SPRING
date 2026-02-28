package com.punithan_library.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punithan_library.Entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    
}
