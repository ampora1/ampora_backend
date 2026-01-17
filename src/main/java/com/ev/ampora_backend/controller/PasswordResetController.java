package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.PasswordResetGenericResDto;
import com.ev.ampora_backend.service.IPasswordResetService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-reset")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PasswordResetController {

    private final IPasswordResetService passwordResetService;

    @PostMapping("/send-verification-email")
    public ResponseEntity<PasswordResetGenericResDto> sendVerificationEmail(@RequestParam String email) throws MessagingException {
        passwordResetService.sendPasswordResetEmail(email);
        return new ResponseEntity<>(
                new PasswordResetGenericResDto(
                        200,
                        "Verification email sent successfully",
                        null
                ),
                HttpStatus.OK
        );
    }
}
