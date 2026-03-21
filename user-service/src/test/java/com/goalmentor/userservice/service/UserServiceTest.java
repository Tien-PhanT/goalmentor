package com.goalmentor.userservice.service;


import com.goalmentor.userservice.dto.UserRequest;
import com.goalmentor.userservice.dto.UserResponse;
import com.goalmentor.userservice.entity.Role;
import com.goalmentor.userservice.entity.User;
import com.goalmentor.userservice.mapper.UserMapper;
import com.goalmentor.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldReturnUserResponse() {
        UserRequest request = new UserRequest();
        request.setEmail("user@test.com");
        request.setUsername("user");
        request.setPassword("password123");

        User user = User.builder()
                .email("user@test.com")
                .username("user")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .email("user@test.com")
                .username("user")
                .role(Role.USER)
                .build();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.createUser(request);

        assertThat(result.getEmail()).isEqualTo("user@test.com");
        assertThat(result.getRole()).isEqualTo(Role.USER);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user = User.builder().id(1L).email("user@test.com").build();
        UserResponse response = UserResponse.builder().id(1L).email("user@test.com").build();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("user@test.com");
    }
}
