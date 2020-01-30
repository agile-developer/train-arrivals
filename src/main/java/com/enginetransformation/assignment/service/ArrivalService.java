package com.enginetransformation.assignment.service;

import com.enginetransformation.assignment.client.TransportApiClient;
import com.enginetransformation.assignment.model.Arrival;
import com.enginetransformation.assignment.model.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ArrivalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrivalService.class);

    private final TransportApiClient apiClient;

    public ArrivalService(TransportApiClient apiClient) {

        this.apiClient = apiClient;
    }

    public List<Station> searchStationsByName(String name) {

        LOGGER.debug("Searching stations by name: {}", name);

        return apiClient.searchStationsByName(name);
    }

    public List<Arrival> getArrivalsForStation(Station station) {

        LOGGER.debug("Fetching arrivals for station: {}: ", station.getName());

        return apiClient.getArrivalsForNaptanId(station.getId());
    }
}
