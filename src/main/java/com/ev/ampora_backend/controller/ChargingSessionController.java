package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.ChargingSessionRequestDTO;
import com.ev.ampora_backend.dto.ChargingSessionResponseDTO;
import com.ev.ampora_backend.service.ChargingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chargersession")
@RequiredArgsConstructor
public class ChargingSessionController {
    private  final ChargingSessionService chargingSessionService;

    @PostMapping
    public ChargingSessionResponseDTO create(@RequestBody ChargingSessionRequestDTO dto){
        return  chargingSessionService.create(dto);
    }
}
