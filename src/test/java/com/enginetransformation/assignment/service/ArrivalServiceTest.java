package com.enginetransformation.assignment.service;


import com.enginetransformation.assignment.client.TransportApiClient;
import com.enginetransformation.assignment.model.Arrival;
import com.enginetransformation.assignment.model.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArrivalServiceTest {

    private ArrivalService arrivalService;

    @Mock
    private TransportApiClient apiClient;

    private Station gpsStation;

    @BeforeEach
    private void init() {

        this.arrivalService = new ArrivalService(apiClient);
        gpsStation = new Station();
        gpsStation.setId("940GZZLUGPS");
        gpsStation.setName("Great Portland Street");
    }

    @Test
    @DisplayName("Search for valid station name returns result")
    public void search_for_valid_station_name_returns_result() {

        String search = "Portland";

        when(apiClient.searchStationsByName(search)).thenReturn(Collections.singletonList(gpsStation));

        List<Station> stations = arrivalService.searchStationsByName(search);

        verify(apiClient, only()).searchStationsByName(search);
        assertNotNull(stations);
        assertEquals(1, stations.size());
        assertEquals(gpsStation, stations.get(0));
    }

    @Test
    @DisplayName("Search for invalid station name returns no result")
    public void search_for_invalid_station_name_returns_no_result() {

        when(apiClient.searchStationsByName(anyString())).thenReturn(Collections.emptyList());

        List<Station> stations = arrivalService.searchStationsByName("unknown");

        verify(apiClient, only()).searchStationsByName("unknown");
        assertNotNull(stations);
        assertTrue(stations.isEmpty());
    }

    @Test
    @DisplayName("Get arrivals for valid naptanId returns results")
    public void get_arrivals_for_valid_naptanId_returns_results() {

        Arrival arrival1 = mock(Arrival.class);
        Arrival arrival2 = mock(Arrival.class);
        Arrival arrival3 = mock(Arrival.class);

        List<Arrival> expected = List.of(arrival1, arrival2, arrival3);

        when(apiClient.getArrivalsForNaptanId(anyString())).thenReturn(expected);

        List<Arrival> arrivals = arrivalService.getArrivalsForStation(gpsStation);

        verify(apiClient, only()).getArrivalsForNaptanId("940GZZLUGPS");
        assertNotNull(arrivals);
        assertSame(arrivals, expected);
    }

    @Test
    @DisplayName("Get arrivals for invalid naptanId returns no result")
    public void get_arrivals_for_invalid_naptanId_returns_result() {

        Station unknown = mock(Station.class);
        when(unknown.getId()).thenReturn("unknown");
        when(unknown.getName()).thenReturn("unknown");
        when(apiClient.getArrivalsForNaptanId(unknown.getId())).thenReturn(Collections.emptyList());

        List<Arrival> arrivals = arrivalService.getArrivalsForStation(unknown);

        verify(apiClient, only()).getArrivalsForNaptanId(unknown.getId());
        assertNotNull(arrivals);
        assertTrue(arrivals.isEmpty());
    }
}
