package com.ev.ampora_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetGenericResDto {
    private int code;
    private String message;
    private Object object;
}
