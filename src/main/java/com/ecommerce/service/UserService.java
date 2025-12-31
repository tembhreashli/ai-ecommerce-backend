package com.ecommerce.service;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.model.User;

import java.util.List;

/**
 * User Service Interface
 */
public interface UserService {

    UserDTO getUserById(Long userId);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long userId, UserDTO userDTO);

    void deleteUser(Long userId);

    User getCurrentUser();

    Long getCurrentUserId();
}
