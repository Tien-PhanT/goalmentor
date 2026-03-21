package com.goalmentor.userservice.mapper;

import com.goalmentor.userservice.dto.UserRequest;
import com.goalmentor.userservice.dto.UserResponse;
import com.goalmentor.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(com.goalmentor.userservice.entity.Role.USER)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}
