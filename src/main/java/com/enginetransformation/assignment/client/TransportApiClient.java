package com.enginetransformation.assignment.client;

import com.enginetransformation.assignment.model.Arrival;
import com.enginetransformation.assignment.model.Station;

import java.util.List;

public interface TransportApiClient {

    List<Station> searchStationsByName(String name);

    List<Arrival> getArrivalsForNaptanId(String naptanId);
}
