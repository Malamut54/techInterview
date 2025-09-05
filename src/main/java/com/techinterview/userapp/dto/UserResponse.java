package com.techinterview.userapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    
    private Long id;
    private String userName;
    private String email;
    private double balance;
    private String userUuid;    
    private Boolean isActive;
}
