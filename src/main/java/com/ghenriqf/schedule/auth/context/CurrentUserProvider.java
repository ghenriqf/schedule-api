package com.ghenriqf.schedule.auth.context;

import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.auth.repository.UserRepository;
import com.ghenriqf.schedule.auth.security.JWTUserData;
import com.ghenriqf.schedule.common.exception.InvalidTokenException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.common.exception.UnauthenticatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UnauthenticatedException("The user is not authenticated in the system");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new InvalidTokenException("The authentication data is in an invalid format");
        }

        return userRepository.findById(jwtUserData.id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}