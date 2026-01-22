package com.ghenriqf.schedule.auth.context;

import com.ghenriqf.schedule.auth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            throw new IllegalStateException("Principal inválido");
        }

        return user;
    }
}
