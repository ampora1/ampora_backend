package com.ev.ampora_backend.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
}
