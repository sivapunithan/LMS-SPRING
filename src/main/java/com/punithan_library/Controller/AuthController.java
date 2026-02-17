package com.punithan_library.Controller;

import com.punithan_library.Exception.UserException;
import com.punithan_library.Payload.DTO.UserDTO;
import com.punithan_library.Payload.Request.ForgotPasswordRequest;
import com.punithan_library.Payload.Request.LoginRequest;
import com.punithan_library.Payload.Request.ResetPasswordRequest;
import com.punithan_library.Payload.Response.ApiResponse;
import com.punithan_library.Payload.Response.AuthResponse;
import com.punithan_library.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController @RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> signUpHandler(@RequestBody @Valid UserDTO request) throws UserException {
        AuthResponse response = authService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody @Valid LoginRequest request) throws UserException {
        AuthResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPasswordHandler(@RequestBody @Valid ForgotPasswordRequest request) throws UserException {
        authService.createPasswordResetToken(request.getEmail());
        ApiResponse response = new ApiResponse("A Reset link was sent to your email.", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> forgotPasswordHandler(@RequestBody @Valid ResetPasswordRequest request) throws UserException, Exception {
        authService.resetPassword(request.getToken(), request.getPassword());
        ApiResponse response = new ApiResponse("Password reset successfully", true);
        return ResponseEntity.ok(response);
    }
}
