package com.ev.ampora_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetReqDto {
    private String newPassword;
    private String confirmPassword;
}