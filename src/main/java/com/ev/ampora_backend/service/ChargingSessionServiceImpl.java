package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.ChargingSessionRequestDTO;
import com.ev.ampora_backend.dto.ChargingSessionResponseDTO;
import com.ev.ampora_backend.entity.Charger;
import com.ev.ampora_backend.entity.ChargingSession;
import com.ev.ampora_backend.entity.Station;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.repository.ChargerRepository;
import com.ev.ampora_backend.repository.ChargingSessionRepository;
import com.ev.ampora_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ChargingSessionServiceImpl implements  ChargingSessionService{
     private  final ChargingSessionRepository chargingSessionRepo;
     private  final UserRepository userRepo;
     private  final ChargerRepository chargerRepo;

     private  ChargingSessionResponseDTO toDto(ChargingSession c){
         return  ChargingSessionResponseDTO.builder().sessionId(c.getSessionId()).userId(c.getUser().getUserId()).chargerId(c.getCharger().getChargerId()).energyUsedKwh(c.getEnergyUsedKwh()).cost(c.getCost()).startTime(c.getStartTime()).endTime(c.getEndTime()).sessionStatus(c.getSessionStatus()).build();

     }

    @Override
    public ChargingSessionResponseDTO create(ChargingSessionRequestDTO dto) {
        Charger c = chargerRepo.findById(dto.getChargerId()).orElseThrow(() -> new RuntimeException("Charger not found"));
        User user=userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User Not Found"));
        ChargingSession chargingSession= ChargingSession.builder().user(user).charger(c).energyUsedKwh(dto.getEnergyUsedKwh()).cost(dto.getCost()).startTime(dto.getStartTime()).endTime(dto.getEndTime()).sessionStatus(dto.getSessionStatus()).build();
        chargingSessionRepo.save(chargingSession);
        return toDto(chargingSession);
     }

    @Override
    public ChargingSessionRequestDTO update(String id, ChargingSessionRequestDTO dto) {
        return null;
    }

    @Override
    public ChargingSessionResponseDTO getSessionById(String id) {
        return null;
    }

    @Override
    public List<ChargingSessionResponseDTO> getAllSession(ChargingSessionRequestDTO dto) {
        return List.of();
    }

    @Override
    public void deleteSession(String id) {

    }
}
