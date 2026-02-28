package com.punithan_library.Service.Impl;

import com.punithan_library.Domain.FineStatus;
import com.punithan_library.Domain.FineType;
import com.punithan_library.Domain.PaymentGateway;
import com.punithan_library.Domain.PaymentType;
import com.punithan_library.Entity.BookLoanEntity;
import com.punithan_library.Entity.FineEntity;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Mapper.FineMapper;
import com.punithan_library.Payload.DTO.FineDTO;
import com.punithan_library.Payload.Request.CreateFineRequest;
import com.punithan_library.Payload.Request.PaymentInitiateRequest;
import com.punithan_library.Payload.Request.WaiveFineRequest;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;
import com.punithan_library.Repository.BookLoanRepository;
import com.punithan_library.Repository.FineRepository;
import com.punithan_library.Service.FineService;
import com.punithan_library.Service.PaymentService;
import com.punithan_library.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final BookLoanRepository bookLoanRepository;
    private final FineRepository fineRepository;
    private final FineMapper fineMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public FineDTO createFine(CreateFineRequest request) throws Exception {

        // 1. Validate book loan exist
        BookLoanEntity bookLoan = bookLoanRepository.findById(request.getBookLoanId())
                .orElseThrow(() -> new Exception("Book Loan Not Found"));
        // 2. Create fine
        FineEntity fine = FineEntity.builder()
                .bookLoan(bookLoan)
                .user(bookLoan.getUser())
                .type(request.getType())
                .amount(request.getAmount())
                .status(FineStatus.PENDING)
                .reason(request.getReason())
                .note(request.getNotes())
                .build();
        FineEntity savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public PaymentInitiateResponse payFine(Long fineId) throws Exception {

        // 1. validate fine exists
        FineEntity fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new Exception("Fine not found"));
        // 2. check fine paid
        if (fine.getStatus() == FineStatus.PAID){
            throw new Exception("Fine already paid");
        }
        // 3. check fine waived
        if (fine.getStatus() == FineStatus.WAIVED){
            throw new Exception("Fine already waived");
        }
        UserEntity user = userService.getCurrentUser();

        PaymentInitiateRequest request = PaymentInitiateRequest.builder()
                .userId(user.getId())
                .fineId(fine.getId())
                .paymentType(PaymentType.FINE)
                .paymentGateway(PaymentGateway.RAZORPAY)
                .amount(fine.getAmount())
                .description("Library Fine payment")
                .build();

        return paymentService.initiatePayment(request);
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception {

        FineEntity fine = fineRepository.findById(fineId).orElseThrow(()-> new Exception("Fine not found"));

        // Apply payment amount safely
        fine.applyPayment(amount);
        fine.setTransactionId(transactionId);
        fine.setStatus(FineStatus.PAID);
        fine.setUpdatedAt(LocalDateTime.now());

        fineRepository.save(fine);

    }

    @Override
    public FineDTO waiveFine(WaiveFineRequest waiveFineRequest) throws Exception {

        FineEntity fine = fineRepository.findById(waiveFineRequest.getFineId()).orElseThrow(()->new Exception("Fine not found"));

        // Check if already waived or paid
        if (fine.getStatus() == FineStatus.WAIVED || fine.getStatus() == FineStatus.PAID){
            throw new Exception("Fine already waived or paid");
        }

        // Waive the fine
        UserEntity currentAdmin = userService.getCurrentUser();
        fine.waive(currentAdmin, waiveFineRequest.getReason());

        // save and return
        FineEntity savedFine = fineRepository.save(fine);

        return fineMapper.toDTO(savedFine);

    }

    @Override
    public List<FineDTO> getMyFines(FineStatus status, FineType fineType) {

        UserEntity user = userService.getCurrentUser();
        List<FineEntity> fines;

        // Apply filters based on parameters
        if (status != null && fineType != null){
            fines = fineRepository.findByUserId(user.getId())
                    .stream().filter(fine -> fine.getStatus() == status && fine.getType() == fineType)
                    .collect(Collectors.toList());
        } else if (status != null){
            fines = fineRepository.findByUserId(user.getId())
                    .stream().filter(fine -> fine.getStatus() == status)
                    .collect(Collectors.toList());
        } else if (fineType != null){
            fines = fineRepository.findByUserIdAndType(user.getId(), fineType)
                    .stream().filter(fine -> fine.getType() == fineType)
                    .collect(Collectors.toList());
        } else {
            fines = fineRepository.findByUserId(user.getId());
        }
        return fines.stream().map(fine -> fineMapper.toDTO(fine))
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<FineDTO> getAllFines(FineStatus status, FineType fineType, Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FineEntity> finePage = fineRepository.findAllWithFilters(userId, status, fineType, pageable);
        return convertToPageResponse(finePage);
    }

    private PageResponse<FineDTO> convertToPageResponse(Page<FineEntity> finePage){

        List<FineDTO> fineDTOS = finePage.getContent()
                .stream().map(fineContent -> fineMapper.toDTO(fineContent)).collect(Collectors.toList());

        return new PageResponse<>(
                fineDTOS,
                finePage.getNumber(),
                finePage.getSize(),
                finePage.getTotalElements(),
                finePage.getTotalPages(),
                finePage.isLast(),
                finePage.isFirst(),
                finePage.isEmpty()
        );
    }
}
