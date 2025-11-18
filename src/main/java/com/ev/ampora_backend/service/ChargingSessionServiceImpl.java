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
    public ChargingSessionResponseDTO update(String id, ChargingSessionRequestDTO dto) {
        ChargingSession cs =chargingSessionRepo.findById(id).orElseThrow(() -> new RuntimeException("charging session is not found"));
        User user =userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Charger charger=chargerRepo.findById(dto.getChargerId()).orElseThrow(() -> new RuntimeException("Charger Not found"));
        cs.setSessionId(id);
        cs.setUser(user);
        cs.setCharger(charger);
        cs.setEnergyUsedKwh(dto.getEnergyUsedKwh());
        cs.setCost(dto.getCost());
        cs.setStartTime(dto.getStartTime());
        cs.setEndTime(dto.getEndTime());
        cs.setSessionStatus(dto.getSessionStatus());
        chargingSessionRepo.save(cs);
        return  toDto(cs);
    }

    @Override
    public ChargingSessionResponseDTO getSessionById(String id) {
         ChargingSession cs = chargingSessionRepo.findById(id).orElseThrow(() -> new RuntimeException("Charger Session not found"));
         return  toDto(cs);
    }

    @Override
    public List<ChargingSessionResponseDTO> getAllSession() {
         return  chargingSessionRepo.findAll().stream().map(this::toDto).toList();
    }


    @Override
    public void deleteSession(String id) {
         chargingSessionRepo.deleteById(id);
    }
}
