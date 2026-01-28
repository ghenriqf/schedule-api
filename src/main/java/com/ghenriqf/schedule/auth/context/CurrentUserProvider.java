package com.ghenriqf.schedule.auth.context;

import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.auth.repository.UserRepository;
import com.ghenriqf.schedule.auth.security.JWTUserData;
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
            throw new IllegalStateException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new IllegalStateException("Principal inválido");
        }

        return userRepository.findById(jwtUserData.id())
                .orElseThrow(() ->
                        new IllegalStateException("Usuário não encontrado"));
    }
}