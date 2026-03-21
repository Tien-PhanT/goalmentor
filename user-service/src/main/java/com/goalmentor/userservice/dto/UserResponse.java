package com.goalmentor.userservice.dto;

import com.goalmentor.userservice.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String fullName;
    private String avatarUrl;
    private Role role;
    private LocalDateTime createdAt;
}
