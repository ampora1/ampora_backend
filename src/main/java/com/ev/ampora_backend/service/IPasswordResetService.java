package com.ev.ampora_backend.service;

import jakarta.mail.MessagingException;

public interface IPasswordResetService {
    void sendPasswordResetEmail(String email) throws MessagingException;
}
