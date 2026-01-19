package com.ghenriqf.schedule.controller;

import com.ghenriqf.schedule.config.security.TokenService;
import com.ghenriqf.schedule.dto.request.LoginRequest;
import com.ghenriqf.schedule.dto.request.UserRequest;
import com.ghenriqf.schedule.dto.response.LoginResponse;
import com.ghenriqf.schedule.dto.response.UserResponse;
import com.ghenriqf.schedule.entity.User;
import com.ghenriqf.schedule.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
   public ResponseEntity<LoginResponse> login (@Valid @RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());

            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            User user = (User) authentication.getPrincipal();
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp (@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

}
