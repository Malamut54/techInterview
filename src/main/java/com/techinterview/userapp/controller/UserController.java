package com.techinterview.userapp.controller;

import com.techinterview.userapp.dto.CreateUserRequest;
import com.techinterview.userapp.dto.UserResponse;
import com.techinterview.userapp.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserManagementService userManagementService;
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserResponse user = userManagementService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            System.out.println("Error creating user: " +  e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<UserResponse> user = userManagementService.getUserById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(required = false) String sortBy) {
        
        List<UserResponse> allUsers = userManagementService.getAllUsers();
        
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equals("name")) {
                allUsers.sort((u1, u2) -> u1.getUserName().compareTo(u2.getUserName()));
            } else if (sortBy.equals("email")) {
                allUsers.sort((u1, u2) -> u1.getEmail().compareTo(u2.getEmail()));
            } else if (sortBy.equals("balance")) {
                allUsers.sort((u1, u2) -> Double.compare(u1.getBalance(), u2.getBalance()));
            } else if (sortBy.equals("balance_desc")) {
                allUsers.sort((u1, u2) -> Double.compare(u2.getBalance(), u1.getBalance()));
            } else if (sortBy.equals("active")) {
                allUsers.sort((u1, u2) -> {
                    if (u1.getIsActive() && !u2.getIsActive()) return -1;
                    if (!u1.getIsActive() && u2.getIsActive()) return 1;
                    return 0;
                });
            } else if (sortBy.equals("id")) {
                allUsers.sort((u1, u2) -> Long.compare(u1.getId(), u2.getId()));
            } else {
                throw new RuntimeException();
            }
        }
        
        return ResponseEntity.ok(allUsers);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<String> getUserStats() {
        return ResponseEntity.ok("Active users: 5");
    }
}