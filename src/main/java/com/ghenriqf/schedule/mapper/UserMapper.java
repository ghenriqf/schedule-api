package com.ghenriqf.schedule.mapper;

import com.ghenriqf.schedule.dto.request.UserRequest;
import com.ghenriqf.schedule.dto.response.UserResponse;
import com.ghenriqf.schedule.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toEntity (UserRequest request) {
        return User
                .builder()
                .username(request.username())
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .birth(request.birth())
                .build();
    }

    public static UserResponse toResponse (User entity) {
        return UserResponse
                .builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build();
    }
}
