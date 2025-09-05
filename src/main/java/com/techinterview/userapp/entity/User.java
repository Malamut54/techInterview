package com.techinterview.userapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name", nullable = false)
    private String userName;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "balance")
    private double balance;
    
    @Column(name = "user_uuid")
    private String userUuid;
    
    @Column(name = "is_active")
    private Boolean isActive;
}
