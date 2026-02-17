package com.punithan_library.Payload.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ForgotPasswordRequest {

    @NotNull(message = "Email is mandatory")
    private String email;
}
