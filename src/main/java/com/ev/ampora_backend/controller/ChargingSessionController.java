package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.ChargingSessionRequestDTO;
import com.ev.ampora_backend.dto.ChargingSessionResponseDTO;
import com.ev.ampora_backend.service.ChargingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chargersession")
@RequiredArgsConstructor
public class ChargingSessionController {
    private  final ChargingSessionService chargingSessionService;

    @PostMapping
    public ChargingSessionResponseDTO create(@RequestBody ChargingSessionRequestDTO dto){
        return  chargingSessionService.create(dto);
    }

    @GetMapping
    public ResponseEntity<List<ChargingSessionResponseDTO>> getAllSession(){
        return ResponseEntity.ok(chargingSessionService.getAllSession());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingSessionResponseDTO> getSessionById(@PathVariable String id){
        return ResponseEntity.ok(chargingSessionService.getSessionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable String id){
        chargingSessionService.deleteSession(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ChargingSessionResponseDTO> updatesession(@PathVariable String id,@RequestBody  ChargingSessionRequestDTO dto){
         return  ResponseEntity.ok(chargingSessionService.update(id,dto));
    }
}
