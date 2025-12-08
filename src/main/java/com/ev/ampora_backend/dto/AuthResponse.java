package com.ev.ampora_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthResponse {
    private String token;
    private String user_id;
}
