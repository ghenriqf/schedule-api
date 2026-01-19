package com.ghenriqf.schedule.auth.service;

import com.ghenriqf.schedule.auth.dto.request.UserRequest;
import com.ghenriqf.schedule.auth.dto.response.UserResponse;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.auth.mapper.UserMapper;
import com.ghenriqf.schedule.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse save (UserRequest request) {
        User entity = UserMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        User save = userRepository.save(entity);
        return UserMapper.toResponse(save);
    }

}
