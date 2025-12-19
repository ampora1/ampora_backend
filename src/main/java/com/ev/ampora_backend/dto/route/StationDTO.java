package com.ev.ampora_backend.dto.route;

import lombok.Data;

@Data
public class StationDTO {
    private String stationId;
    private String name;
    private String address;
    private double lat;
    private double lon;
    private double powerKw;
    private double distanceToRouteKm;
}
