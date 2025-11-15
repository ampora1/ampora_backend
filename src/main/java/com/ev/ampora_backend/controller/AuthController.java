package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.LoginRequest;
import com.ev.ampora_backend.dto.RegisterRequest;
import com.ev.ampora_backend.dto.UserDTO;
import com.ev.ampora_backend.dto.UserResponse;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        return new UserResponse(
                user.getUserId().toString(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name()
        );
    }
}
