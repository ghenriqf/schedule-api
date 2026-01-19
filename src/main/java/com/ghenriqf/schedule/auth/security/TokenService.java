package com.ghenriqf.schedule.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ghenriqf.schedule.auth.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

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

    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT decode = JWT
                    .require(algorithm)
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData
                    .builder()
                    .id(decode.getClaim("id").asLong())
                    .email(decode.getSubject())
                    .build());
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
