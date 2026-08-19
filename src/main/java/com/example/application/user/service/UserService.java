package com.example.application.user.service;


import com.example.application.user.dto.AuthResponse;
import com.example.application.user.dto.LoginRequest;
import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;

import java.util.*;

public interface UserService {

 UserResponse register(UserRequest request);

 AuthResponse login(LoginRequest request);

 UserResponse getById(Long id);

 UserResponse deleteUserById(Long id);

 List<UserResponse> getAllUsers();
}
