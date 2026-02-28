package com.punithan_library.Service.Impl;

import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Mapper.UserMapper;
import com.punithan_library.Payload.DTO.UserDTO;
import com.punithan_library.Repository.UserRepository;
import com.punithan_library.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream().map(
                UserMapper::toDTO
        ).collect(Collectors.toList());
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new UsernameNotFoundException("User not found")
        );
    }
}
