package com.ev.ampora_backend.controller;

import com.ev.ampora_backend.dto.StationRequestDTO;
import com.ev.ampora_backend.dto.StationResponseDTO;
import com.ev.ampora_backend.entity.Station;
import com.ev.ampora_backend.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://15.134.60.252, http://localhost:5173,https://ampora.dev")
public class StationController {
    private final StationService stationService;

    @PostMapping
    public boolean create(@RequestBody StationRequestDTO dto){
        return stationService.create(dto);
    }
    @PostMapping("/bulk")
    public ResponseEntity<?> createBulk(@RequestBody List<StationRequestDTO> stations) {

        if (stations == null || stations.isEmpty()) {
            return ResponseEntity.badRequest().body("Station list cannot be empty");
        }

        stationService.createBulk(stations);
        return ResponseEntity.ok("Stations inserted successfully");
    }
    @GetMapping
    public ResponseEntity<List<StationResponseDTO>> getAll(){
        return ResponseEntity.ok(stationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponseDTO> getOne(@PathVariable String id){
        return ResponseEntity.ok(stationService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationResponseDTO> update(@PathVariable String id,@RequestBody StationRequestDTO dto){
        return ResponseEntity.ok(stationService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        stationService.delete(id);
        return ResponseEntity.ok("Station Deleted");
    }

}
