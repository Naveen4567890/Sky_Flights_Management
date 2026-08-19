package com.example.application.user.service;

import com.example.application.user.dto.AuthResponse;
import com.example.application.user.dto.LoginRequest;
import com.example.application.user.dto.UserRequest;
import com.example.application.user.dto.UserResponse;
import com.example.application.user.entity.User;
import com.example.application.user.exception.UserNotFoundException;
import com.example.application.user.mapper.UserMapper;
import com.example.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRequest request) {
        try{
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            User user= UserMapper.toEntity(request);

            user.setUserId(generateUserId());

            String hashedPassword =
                    passwordEncoder.encode(request.getPassword());

            user.setPassword(hashedPassword);

            User savedUser=userRepository.save(user);

            return UserMapper.toResponse(savedUser);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse getById(Long id) {
       try{
           User user = userRepository.findById(id)
                   .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
           return UserMapper.toResponse(user);
       }
       catch (UserNotFoundException e){
           throw e;
       }
        catch (Exception e) {
            throw new RuntimeException("Error fetching user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserResponse> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream().map(UserMapper::toResponse).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
    }

   private String generateUserId() {
        try {
            long count=userRepository.count()+1;
            return String.format("User%04d",count);
        } catch (Exception e) {
            throw new RuntimeException("Error generating user ID: " + e.getMessage(), e);
        }
   }

    @Override
    public AuthResponse login(LoginRequest request) {

        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            String token = jwtService.generateToken(user.getEmail());

            return AuthResponse.builder()
                    .token(token)
                    .userId(user.getUserId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build();
        } catch (UserNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error during login: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse deleteUserById(Long id) {
        try{
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            userRepository.delete(user);

            return UserMapper.toResponse(user);
        }
        catch (UserNotFoundException e){
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

}