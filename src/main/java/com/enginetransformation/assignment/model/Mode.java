package com.enginetransformation.assignment.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mode {

    TUBE("tube"), BUS("bus");

    private String name;

    Mode(String name) {

        this.name = name;
    }

    @JsonValue
    public String getName() {

        return name;
    }
}
