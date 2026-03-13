package com.goalmentor.userservice.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String username;
    private String password;
    private String fullName;
}
