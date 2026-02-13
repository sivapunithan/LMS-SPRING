package com.punithan_library.Service.Impl;

import com.punithan_library.Config.Jwtprovider;
import com.punithan_library.Domain.UserRole;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Exception.UserException;
import com.punithan_library.Mapper.UserMapper;
import com.punithan_library.Payload.DTO.UserDTO;
import com.punithan_library.Payload.Response.AuthResponse;
import com.punithan_library.Repository.UserRepository;
import com.punithan_library.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Jwtprovider jwtprovider;

    @Override
    public AuthResponse login(String username, String password) {
        Authentication authentication = authenticate(username, password);
        return null;
    }

    private Authentication authenticate(String username, String password) {
        return null;
    }

    @Override
    public AuthResponse signUp(UserDTO req) throws UserException {
        UserEntity userEntity = userRepository.findByEmail(req.getEmail());
        if (userEntity == null){
            throw new UserException("Email id Not found");
        }
        UserEntity createUser = new UserEntity();
        createUser.setEmail(req.getEmail());
        createUser.setPassword(passwordEncoder.encode(req.getPassword()));
        createUser.setPhone(req.getPhone());
        createUser.setFullName(req.getFullName());
        createUser.setLastLogin(LocalDateTime.now());
        createUser.setRole(UserRole.ROLE_USER);

        UserEntity savedUser = userRepository.save(createUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtprovider.generateToken(authentication);
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setTitle("Welcome "+ createUser.getFullName());
        response.setMessage("Registered Successfully");
        response.setUser(UserMapper.toDTO(savedUser));

        return response;
    }

    @Override
    public void createPasswordResetToken(String email) {

    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }
}
