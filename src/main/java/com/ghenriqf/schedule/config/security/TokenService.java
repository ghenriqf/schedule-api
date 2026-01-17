package com.ghenriqf.schedule.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ghenriqf.schedule.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken (User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withExpiresAt(Instant.now().plusSeconds(3600)) // 1 hour
                .withIssuedAt(Instant.now())
                .withIssuer("ScheduleApp")
                .sign(algorithm);
    }
}
