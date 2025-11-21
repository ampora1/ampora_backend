package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.ChargingSessionRequestDTO;
import com.ev.ampora_backend.dto.ChargingSessionResponseDTO;

import java.util.List;

public interface ChargingSessionService {
    ChargingSessionResponseDTO create(ChargingSessionRequestDTO dto);
    ChargingSessionResponseDTO update(String id,ChargingSessionRequestDTO dto);
    ChargingSessionResponseDTO getSessionById(String id);
    List<ChargingSessionResponseDTO> getAllSession();
    void deleteSession(String id);

}
