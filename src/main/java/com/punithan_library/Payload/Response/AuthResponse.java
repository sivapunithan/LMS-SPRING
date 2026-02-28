package com.punithan_library.Payload.Response;

import com.punithan_library.Payload.DTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private String message;
    private String title;
    private String role;
    private UserDTO user;

}
