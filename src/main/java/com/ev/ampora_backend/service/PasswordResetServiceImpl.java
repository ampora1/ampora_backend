package com.ev.ampora_backend.service;

import com.ev.ampora_backend.repository.StationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements IPasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    private final String NUMBERS = "0123456789";

    @Override
    public void sendPasswordResetEmail(String email) throws MessagingException {
        int verificationCode = generateCode(5);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("maheshiwickramarathna23@gmail.com");
        helper.setTo(email);
        helper.setSubject("Reset Your Ampora Password");

        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    .container {
                        font-family: Arial, sans-serif;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                    }
                    .verification-code {
                        background-color: #f4f4f4;
                        padding: 15px;
                        font-size: 24px;
                        font-weight: bold;
                        text-align: center;
                        margin: 20px 0;
                        letter-spacing: 2px;
                        border-radius: 5px;
                    }
                    .message {
                        color: #666;
                        line-height: 1.6;
                        margin-bottom: 20px;
                    }
                    .note {
                        font-size: 12px;
                        color: #999;
                        margin-top: 20px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Email Verification</h2>
                    <div class="message">
                        <p>Please use the verification code below to reset your password:</p>
                    </div>
                    <div class="verification-code">%d</div>
                    <div class="message">
                        <p>If you didn't request this verification, please ignore this email.</p>
                    </div>
                    <div class="note">
                        This code will expire in 10 minutes for security purposes.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(verificationCode);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }



    private int generateCode(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            char selectedNumber = NUMBERS.charAt(random.nextInt(10));
            if (i == 0 && selectedNumber == '0') {
                selectedNumber = NUMBERS.charAt(random.nextInt(9) + 1); // Avoid leading zero
            }
            sb.append(selectedNumber);
        }

        return Integer.parseInt(sb.toString());
    }
}
