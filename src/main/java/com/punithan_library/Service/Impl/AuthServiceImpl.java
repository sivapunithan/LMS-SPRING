package com.punithan_library.Service.Impl;

import com.punithan_library.Config.Jwtprovider;
import com.punithan_library.Domain.UserRole;
import com.punithan_library.Entity.PasswordResetToken;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Exception.UserException;
import com.punithan_library.Mapper.UserMapper;
import com.punithan_library.Payload.DTO.UserDTO;
import com.punithan_library.Payload.Response.AuthResponse;
import com.punithan_library.Repository.PasswordResetTokenRepo;
import com.punithan_library.Repository.UserRepository;
import com.punithan_library.Service.AuthService;
import com.punithan_library.Service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Jwtprovider jwtprovider;
    @Autowired
    private CustomUserServiceImplementation customUserServiceImplementation;
    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public AuthResponse login(String username, String password) throws UserException {   // UserName is email here
        try {
            Authentication authentication = authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String role = authorities.iterator().next().getAuthority();
            String token = jwtprovider.generateToken(authentication);

            UserEntity user = userRepository.findByEmail(username);

            // Update last login
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setTitle("Login Success");
            authResponse.setMessage("Welcome back " + username);
            authResponse.setJwt(token);
            authResponse.setRole(role);
            authResponse.setUser(UserMapper.toDTO(user));

            return authResponse;
        } catch (BadCredentialsException e) {
            throw new UserException("Invalid username or password");
        }
    }

//    private Authentication authenticate(String username, String password) throws UserException {
//
//        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
//        if (userDetails == null){
//            throw new UserException("User not found with email - "+password);
//        }
//         if(!passwordEncoder.matches(password, userDetails.getPassword())){
//             throw new UserException("Wrong password");
//         }
//         return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
//
//    }

    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }


    @Override
    public AuthResponse signUp(UserDTO req) throws UserException {
        UserEntity userEntity = userRepository.findByEmail(req.getEmail());
        if (userEntity != null){
            throw new UserException("Email is already registered");
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

    @Transactional
    public void createPasswordResetToken(String email) throws UserException {
        String frontendUrl = "";
        UserEntity user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserException("User not found for this given email");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();
        passwordResetTokenRepo.save(resetToken);

        String resetLink = frontendUrl + token;
        String subject = "Password Reset Request";
        String body = "Your request to reset the password. Use this link (valid 5 minutues)";
        emailService.sendMail(user.getEmail(), subject, body);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(()-> new Exception("Token is not valid"));
        if (resetToken.isExpired()){
            passwordResetTokenRepo.delete(resetToken);
            throw new Exception("Token is expired");
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        passwordResetTokenRepo.save(resetToken);
    }
}
