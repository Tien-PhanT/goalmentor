package com.goalmentor.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goalmentor.userservice.dto.UserRequest;
import com.goalmentor.userservice.dto.UserResponse;
import com.goalmentor.userservice.entity.Role;
import com.goalmentor.userservice.exception.UserNotFoundException;
import com.goalmentor.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser_shouldReturn201() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("user@test.com");
        request.setUsername("user");
        request.setPassword("password123");
        request.setFullName("User FullName");

        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("user")
                .email("user@test.com")
                .role(Role.USER)
                .build();

        when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

    }

    @Test
    void getUserById_shouldReturn200() throws Exception {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .email("user@test.com")
                .username("user")
                .role(Role.USER)
                .build();

        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@test.com"));

    }

    @Test
    void getUserById_shouldReturn404_whenNotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: 99"));
    }

    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .email("user@test.com")
                .username("user")
                .role(Role.USER)
                .build();

        when(userService.getAllUsers()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@test.com"));
    }

    @Test
    void createUser_shouldReturn400_whenEmailIsInvalid() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("invalid-email");
        request.setUsername("user");
        request.setPassword("password123");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is invalid"));

    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("updated@test.com");
        request.setUsername("user");
        request.setPassword("password123");

        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("user")
                .email("updated@test.com")
                .role(Role.USER)
                .build();

        when(userService.updateUser(any(), any())).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@test.com"));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
