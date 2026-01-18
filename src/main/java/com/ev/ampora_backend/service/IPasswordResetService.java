package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.PasswordResetReqDto;
import jakarta.mail.MessagingException;

public interface IPasswordResetService {
    String sendPasswordResetEmail(String email) throws MessagingException;
    String verifyCode(int code, String token);
    void resetPassword(PasswordResetReqDto dto, String token);
}