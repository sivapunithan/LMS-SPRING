package com.punithan_library.Controller;

import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Payload.DTO.UserDTO;
import com.punithan_library.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
