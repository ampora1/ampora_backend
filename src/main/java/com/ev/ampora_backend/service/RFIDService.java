package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.AssignRFIDRequest;
import com.ev.ampora_backend.entity.RFIDCard;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.repository.RFIDCardRepository;
import com.ev.ampora_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RFIDService {

    private final RFIDCardRepository rfidRepo;
    private final UserRepository userRepo;

    public RFIDService(RFIDCardRepository rfidRepo, UserRepository userRepo) {
        this.rfidRepo = rfidRepo;
        this.userRepo = userRepo;
    }

    public RFIDCard assignRFID(AssignRFIDRequest req) {

        // 1️⃣ Validate user
        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Prevent duplicate RFID
        if (rfidRepo.existsByUid(req.getUid())) {
            throw new RuntimeException("RFID already assigned");
        }

        // 3️⃣ Prevent multiple cards per user
        if (rfidRepo.existsByUser_UserId(user.getUserId())) {
            throw new RuntimeException("User already has an RFID card");
        }

        // 4️⃣ Assign card
        RFIDCard card = RFIDCard.builder()
                .uid(req.getUid())
                .user(user)
                .build();

        return rfidRepo.save(card);
    }
}
