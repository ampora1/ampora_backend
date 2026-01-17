package com.ev.ampora_backend.service;

import com.ev.ampora_backend.exception.InvalidPropertyException;
import com.ev.ampora_backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements IPasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    private final String NUMBERS = "0123456789";
    private final Map<String, VerificationData> verificationStorage = new HashMap<>();

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "MySuperSecretKey"; // 16 characters for AES-128

    @Override
    public String sendPasswordResetEmail(String email) throws MessagingException {
        int verificationCode = generateCode(6);
        long expirationTime = System.currentTimeMillis() + (10 * 60 * 1000); // Expire in 10 mins

        verificationStorage.put(email, new VerificationData(verificationCode, expirationTime));

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
                    <h2>Reset Your Ampora Password</h2>
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

        return encrypt(email);
    }


    @Override
    public String verifyCode(int code, String token) {
        String email = decrypt(token);
        VerificationData data = verificationStorage.get(email);

        if (data == null || System.currentTimeMillis() > data.getExpirationTime()) {
            throw new InvalidPropertyException("Verification code has expired or does not exist.");
        }

        if (data.getCode() == code) {
            return encrypt(email);
        } else {
            throw new InvalidPropertyException("Invalid verification code.");
        }
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

    private static class VerificationData {
        private final int code;
        private final long expirationTime;

        public VerificationData(int code, long expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public int getCode() {
            return code;
        }

        public long getExpirationTime() {
            return expirationTime;
        }
    }

    private static String encrypt(String value) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getUrlEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting value", e);
        }
    }

    private static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedValue);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting value", e);
        }
    }
}