package com.enginetransformation.assignment.client;

import com.enginetransformation.assignment.model.Arrival;
import com.enginetransformation.assignment.model.Station;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TflApiClientImpl implements TransportApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TflApiClientImpl.class);

    private static final String SEARCH_STATION = "/StopPoint/Search";
    private static final String ARRIVALS_FOR_STATION = "/StopPoint/{naptanId}/Arrivals";

    private final String BASE_URL;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TflApiClientImpl(String base_url) {

        BASE_URL = base_url;
        httpClient = HttpClient.newHttpClient();
        objectMapper = new JsonMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"));
    }

    @Override
    public List<Station> searchStationsByName(String name) {

        if (name == null || "".equals(name.trim())) return Collections.emptyList();

        HttpRequest.Builder requestBuilder = requestBuilder(BASE_URL + SEARCH_STATION + "/" + name + "?modes=tube");
        HttpRequest request = requestBuilder.GET().build();
        String responseString = executeRequest(request);
        try {
            JsonNode searchResultsNode = objectMapper.readTree(responseString);
            JsonNode matchesNode = searchResultsNode.get("matches");
            if (matchesNode == null || matchesNode.isEmpty()) {
                return Collections.emptyList();
            }
            List<Station> stations = new ArrayList<>();
            ArrayNode matchesArrayNode = (ArrayNode) matchesNode;
            matchesArrayNode.elements().forEachRemaining(matchedStop -> {
                Station station = new Station();
                station.setId(matchedStop.get("id").textValue());
                station.setName(matchedStop.get("name").textValue());
                stations.add(station);
            });

            return stations;
        } catch (JsonProcessingException e) {
            LOGGER.error("Encountered JsonProcessingException while parsing response from TfL API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Arrival> getArrivalsForNaptanId(String naptanId) {

        if (naptanId == null || "".equals(naptanId.trim())) return Collections.emptyList();

        String arrivalsEndpoint = ARRIVALS_FOR_STATION.replace("{naptanId}", naptanId);
        HttpRequest.Builder requestBuilder = requestBuilder(BASE_URL + arrivalsEndpoint);
        HttpRequest request = requestBuilder.GET().build();
        String responseString = executeRequest(request);
        try {
            return objectMapper.readValue(responseString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            LOGGER.error("Encountered JsonProcessingException while parsing response from TfL API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private HttpRequest.Builder requestBuilder(String endpoint) {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint));
        requestBuilder.header("Content-Type", "application/json");
        requestBuilder.header("Accept", "application/json");

        return requestBuilder;
    }

    private String executeRequest(HttpRequest httpRequest) {

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) {
                LOGGER.error("Received HTTP status code: {} from Tfl API.", statusCode);
                throw new RuntimeException(String.format("Received HTTP status code: %d from Tfl API.", statusCode));
            }
            String responseString = response.body();
            if (responseString == null) {
                LOGGER.error("Received null or empty response body from TfL API.");
                throw new RuntimeException("Received null or empty response body from TfL API.");
            }

            return responseString;
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Encountered exception calling TfL API: " + e.getMessage());
            throw new RuntimeException("Encountered exception calling TfL API: " + e.getMessage());
        }
    }
}
