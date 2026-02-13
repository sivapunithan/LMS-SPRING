package com.punithan_library.Service;

import com.punithan_library.Exception.UserException;
import com.punithan_library.Payload.DTO.UserDTO;
import com.punithan_library.Payload.Response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password);
    AuthResponse signUp(UserDTO req) throws UserException;

    void createPasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}
