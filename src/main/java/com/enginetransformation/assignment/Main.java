package com.enginetransformation.assignment;

import com.enginetransformation.assignment.api.ArrivalController;
import com.enginetransformation.assignment.client.TflApiClientImpl;
import com.enginetransformation.assignment.client.TransportApiClient;
import com.enginetransformation.assignment.model.Arrival;
import com.enginetransformation.assignment.model.Station;
import com.enginetransformation.assignment.service.ArrivalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String BASE_URL = "https://api.tfl.gov.uk";

    public static void main(String[] args) throws InterruptedException {

        LOGGER.info("Starting 'Train Arrivals'");

        if (args.length != 1) {
            LOGGER.error("Usage: java -jar /path/to/executable/train-arrivals.jar <station-name>");
            System.exit(1);
        }

        TransportApiClient apiClient = new TflApiClientImpl(BASE_URL);
        ArrivalService arrivalService = new ArrivalService(apiClient);
        ArrivalController arrivalController = new ArrivalController(arrivalService);

        String stationName = args[0];
        LOGGER.info("Searching for stations by name: {}", stationName);
        List<Station> stations = arrivalController.searchStationsByName(stationName);
        if (stations.isEmpty()) {
            LOGGER.info("Could not find stations with name: {}. Program will exit.", stationName);
            System.exit(1);
        }

        displayStations(stationName, stations);
        TimeUnit.SECONDS.sleep(2L);

        Station station = getUserChoice(stations);
        TimeUnit.SECONDS.sleep(2L);

        List<Arrival> arrivals = arrivalController.getArrivalsForStation(station);
        if (arrivals.isEmpty()) {
            LOGGER.info("Could not find arrival information for station: {}. Program will exit.", station.getName());
            System.exit(1);
        }

        displayArrivalsInOrder(station.getName(), arrivals, Comparator.comparingInt(Arrival::getTimeToStation));
//        displayArrivalsInOrder(station.getName(), arrivals, Comparator.comparing(Arrival::getPlatformName));
//        displayArrivalsInOrder(station.getName(), arrivals, Comparator.comparing(Arrival::getLineName));

        LOGGER.info("Exiting 'Train Arrivals'");
    }

    private static void displayStations(String stationName, List<Station> stations) {

        int numStations = stations.size();
        LOGGER.info("Found {} stations matching name: {}", numStations, stationName);
        System.out.println();
        for (int i = 0; i < numStations; i++) {
            System.out.println(i + 1 + ". " + stations.get(i).getName());
        }
    }

    private static Station getUserChoice(List<Station> stations) {

        System.out.println();
        System.out.print("Select station index to retrieve arrivals: ");
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        if (choice < 1 || choice > stations.size()) {
            LOGGER.error("{} is not a valid choice. Program will exit.", choice);
            System.exit(1);
        }

        return stations.get(choice - 1);
    }

    private static void displayArrivalsInOrder(String stationName, List<Arrival> arrivals, Comparator<Arrival> comparator) {

        System.out.println();
        System.out.println("Expected arrivals at station: " + stationName);
        arrivals.sort(comparator);
        arrivals.forEach(arrival -> {
            System.out.println("--------------------------------------------------");
            System.out.println("Line: " + arrival.getLineName());
            System.out.println("Platform: " + arrival.getPlatformName());
            System.out.println("Arriving in: " + arrival.getTimeToStationInMinutes() + " mins.");
            System.out.println("End destination: " + arrival.getDestinationName());
            System.out.println();
        });
    }
}
