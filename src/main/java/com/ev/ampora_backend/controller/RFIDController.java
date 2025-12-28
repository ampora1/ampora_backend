package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.AssignRFIDRequest;
import com.ev.ampora_backend.entity.RFIDCard;
import com.ev.ampora_backend.service.RFIDService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rfid")
@CrossOrigin
public class RFIDController {

    private final RFIDService rfidService;

    public RFIDController(RFIDService rfidService) {
        this.rfidService = rfidService;
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignRFID(@RequestBody AssignRFIDRequest request) {

        RFIDCard card = rfidService.assignRFID(request);

        return ResponseEntity.ok(card);
    }
}
