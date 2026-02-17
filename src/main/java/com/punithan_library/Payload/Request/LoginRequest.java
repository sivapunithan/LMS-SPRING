package com.punithan_library.Payload.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "User name or Email is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;
}
