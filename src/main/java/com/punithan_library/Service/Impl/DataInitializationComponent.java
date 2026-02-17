package com.punithan_library.Service.Impl;

import com.punithan_library.Domain.UserRole;
import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public void initializationAdminUser(){
        String adminEmail = "sivapunithansadha@gmail.com";
        String adminPassword = "sivapunithansadha";

        if (userRepository.findByEmail(adminEmail) == null){
            UserEntity user = UserEntity.builder()
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("Admin Punithan")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            UserEntity admin = userRepository.save(user);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        initializationAdminUser();                 // Whenevr the system re-run this will execute
    }
}
