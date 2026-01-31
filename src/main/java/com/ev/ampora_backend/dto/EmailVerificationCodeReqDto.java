package com.ev.ampora_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerificationCodeReqDto {
    private int code;
}