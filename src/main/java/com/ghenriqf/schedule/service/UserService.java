package com.ghenriqf.schedule.service;

import com.ghenriqf.schedule.dto.request.UserRequest;
import com.ghenriqf.schedule.dto.response.UserResponse;
import com.ghenriqf.schedule.entity.User;
import com.ghenriqf.schedule.mapper.UserMapper;
import com.ghenriqf.schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
