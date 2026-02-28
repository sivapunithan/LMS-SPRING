package com.punithan_library.Service;

import com.punithan_library.Domain.FineStatus;
import com.punithan_library.Domain.FineType;
import com.punithan_library.Payload.DTO.FineDTO;
import com.punithan_library.Payload.Request.CreateFineRequest;
import com.punithan_library.Payload.Request.WaiveFineRequest;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface FineService {

    FineDTO createFine(CreateFineRequest request) throws Exception;

    PaymentInitiateResponse payFine(Long fineId) throws Exception;

    void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception;

    FineDTO waiveFine(WaiveFineRequest waiveFineRequest) throws Exception;

    List<FineDTO> getMyFines(FineStatus status, FineType fineType);

    PageResponse<FineDTO> getAllFines(FineStatus status, FineType fineType, Long userId, int page, int size);

}
