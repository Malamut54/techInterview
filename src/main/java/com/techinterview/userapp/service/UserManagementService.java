package com.techinterview.userapp.service;

import com.techinterview.userapp.dto.CreateUserRequest;
import com.techinterview.userapp.dto.UserResponse;
import com.techinterview.userapp.entity.User;
import com.techinterview.userapp.repository.UserDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserManagementService {
    
    @Autowired
    private UserDataRepository userDataRepository;
    
    @PostConstruct
    public void initializeData() {
        log.info("Initializing user service...");
        loadAllUsers();
    }
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        validateUserData(request);
        
        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .balance(request.getBalance())
                .isActive(request.getIsActive())
                .userUuid(UUID.randomUUID().toString())
                .build();
        
        User savedUser = userDataRepository.save(user);
        return mapToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        return userDataRepository.findById(id)
                .map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<User> users = userDataRepository.findAll();
        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Transactional
    public List<UserResponse> getActiveUsers() {
        List<User> activeUsers = userDataRepository.getActiveUsers(true);
        return activeUsers.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void validateUserData(CreateUserRequest request) {
        if (userDataRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email already exists: " + request.getEmail());
        }
    }
    
    @Transactional(readOnly = true)
    private void loadAllUsers() {
        List<User> users = userDataRepository.findAll();
        log.info("Loaded {} users", users.size());
    }
    
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .balance(user.getBalance())
                .userUuid(user.getUserUuid())
                .isActive(user.getIsActive())
                .build();
    }
}