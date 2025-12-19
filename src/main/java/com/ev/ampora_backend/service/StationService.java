package com.ev.ampora_backend.service;

import com.ev.ampora_backend.dto.ChargerResponseDTO;
import com.ev.ampora_backend.dto.StationRequestDTO;
import com.ev.ampora_backend.dto.StationResponseDTO;
import com.ev.ampora_backend.entity.Station;
import com.ev.ampora_backend.entity.User;
import com.ev.ampora_backend.repository.StationRepository;
import com.ev.ampora_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    private  final UserRepository userRepository;

    public boolean create(StationRequestDTO dto){
        User operator = userRepository.findById(dto.getOperatorId()).orElse(null);
        Station station = Station.builder().name(dto.getName()).address(dto.getAddress()).latitude(dto.getLatitude()).longitude(dto.getLongitude()).status(dto.getStatus()).operator(operator).build();
        stationRepository.save(station);
        return true;
    }

    //get all station
    public List<StationResponseDTO> getAll(){
        return  stationRepository.findAll().stream().map(this::toDTO).toList();
    }

    //get all station by id
    public StationResponseDTO getById(String id){
        Station s = stationRepository.findById(id).orElseThrow(()->new  RuntimeException("Station not found"));
        return toDTO(s);
    }

    //update Station
    public StationResponseDTO update(String id,StationRequestDTO dto){
        Station station =stationRepository.findById(id).orElseThrow(() ->new RuntimeException("station not found"));
        station.setName(dto.getName());
        station.setAddress(dto.getAddress());
        station.setLatitude(dto.getLatitude());
        station.setLongitude(dto.getLongitude());
        station.setStatus(dto.getStatus());
        stationRepository.save(station);
        return toDTO(station);
    }

    //delete user
    public void delete(String id){
        if(!stationRepository.existsById(id)){
            throw new RuntimeException("Station not found");
        }
        stationRepository.deleteById(id);
    }

    private StationResponseDTO toDTO(Station s){
        return StationResponseDTO.builder()
                .stationId(s.getStationId())
                .name(s.getName())
                .address(s.getAddress())
                .latitude(s.getLatitude())
                .longitude(s.getLongitude())
                .chargers(s.getChargers().stream().map(
                        c-> ChargerResponseDTO.builder()
                                .chargerID(c.getChargerId())
                                .build()
                             ).toList()
                ).status(s.getStatus()).build();
    }


}
