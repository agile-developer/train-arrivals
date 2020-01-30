package com.enginetransformation.assignment.api;

import com.enginetransformation.assignment.model.Arrival;
import com.enginetransformation.assignment.model.Station;
import com.enginetransformation.assignment.service.ArrivalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ArrivalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrivalController.class);

    private final ArrivalService arrivalService;

    public ArrivalController(ArrivalService arrivalService) {

        this.arrivalService = arrivalService;
    }

    public List<Station> searchStationsByName(String name) {

        LOGGER.debug("Searching stations by name: {}", name);

        return arrivalService.searchStationsByName(name);
    }

    public List<Arrival> getArrivalsForStation(Station station) {

        LOGGER.debug("Fetching arrivals for station: {}", station.getName());

        return arrivalService.getArrivalsForStation(station);
    }
}
