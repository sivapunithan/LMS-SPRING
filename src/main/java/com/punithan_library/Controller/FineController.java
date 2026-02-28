package com.punithan_library.Controller;


import com.punithan_library.Domain.FineStatus;
import com.punithan_library.Domain.FineType;
import com.punithan_library.Payload.DTO.FineDTO;
import com.punithan_library.Payload.Request.CreateFineRequest;
import com.punithan_library.Payload.Request.WaiveFineRequest;
import com.punithan_library.Payload.Response.PageResponse;
import com.punithan_library.Payload.Response.PaymentInitiateResponse;
import com.punithan_library.Service.FineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/fines")
@RestController
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @PostMapping("/create-fine")
    public ResponseEntity<?> createFine (@Valid @RequestBody CreateFineRequest request) throws Exception {

        FineDTO fine = fineService.createFine(request);
        return  ResponseEntity.ok(fine);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> payFine (@PathVariable Long id) throws Exception {

        PaymentInitiateResponse response = fineService.payFine(id);
        return  ResponseEntity.ok(response);
    }

    @PostMapping("/waive-fine")
    public ResponseEntity<?> waiveFine (@Valid @RequestBody WaiveFineRequest request) throws Exception {

        FineDTO fine = fineService.waiveFine(request);
        return  ResponseEntity.ok(fine);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyFines (@RequestParam(required = false)FineStatus status, @RequestParam(required = false)FineType type) throws Exception {

        List<FineDTO> fines = fineService.getMyFines(status, type);
        return ResponseEntity.ok(fines);
    }

    @GetMapping
    public ResponseEntity<?> getAllFines(@RequestParam(required = false)FineStatus status, @RequestParam(required = false)FineType type,
                                         @RequestParam(required = false)Long userId, @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20")int size) throws Exception {

        PageResponse<FineDTO> fines = fineService.getAllFines(status, type, userId, page, size);
        return ResponseEntity.ok(fines);
    }
}
