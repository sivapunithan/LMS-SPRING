package com.punithan_library.Service;

import com.punithan_library.Entity.UserEntity;
import com.punithan_library.Payload.DTO.UserDTO;

import java.util.List;

public interface UserService {

    public UserEntity getCurrentUser();

    public List<UserDTO> getAllUsers();

    UserEntity findById(Long id);
}
