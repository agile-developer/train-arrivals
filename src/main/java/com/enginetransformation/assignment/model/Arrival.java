package com.enginetransformation.assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Arrival {

    private String id;
    private String naptanId;
    private String stationName;
    private String lineName;
    private String platformName;
    private String destinationName;
    private String towards;
    private Date expectedArrival;
    private int timeToStation;
    @JsonProperty(value = "modeName")
    private Mode mode;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getNaptanId() {

        return naptanId;
    }

    public void setNaptanId(String naptanId) {

        this.naptanId = naptanId;
    }

    public String getStationName() {

        return stationName;
    }

    public void setStationName(String stationName) {

        this.stationName = stationName;
    }

    public String getLineName() {

        return lineName;
    }

    public void setLineName(String lineName) {

        this.lineName = lineName;
    }

    public String getPlatformName() {

        return platformName;
    }

    public void setPlatformName(String platformName) {

        this.platformName = platformName;
    }

    public String getDestinationName() {

        return destinationName;
    }

    public void setDestinationName(String destinationName) {

        this.destinationName = destinationName;
    }

    public String getTowards() {

        return towards;
    }

    public void setTowards(String towards) {

        this.towards = towards;
    }

    public Date getExpectedArrival() {

        return expectedArrival;
    }

    public void setExpectedArrival(Date expectedArrival) {

        this.expectedArrival = expectedArrival;
    }

    public int getTimeToStation() {

        return timeToStation;
    }

    public void setTimeToStation(int timeToStation) {

        this.timeToStation = timeToStation;
    }

    public int getTimeToStationInMinutes() {

        return timeToStation / 60;
    }

    public Mode getMode() {

        return mode;
    }

    public void setMode(Mode mode) {

        this.mode = mode;
    }
}
