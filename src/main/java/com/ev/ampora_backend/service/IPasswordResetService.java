package com.ev.ampora_backend.service;

import jakarta.mail.MessagingException;

public interface IPasswordResetService {
    String sendPasswordResetEmail(String email) throws MessagingException;
    String verifyCode(int code, String token);
}
