package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.*;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        AuthResponse resp = userService.login(request.getEmail(), request.getPassword());
        return resp;
    }
}
