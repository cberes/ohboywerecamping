package com.ohboywerecamping.webapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Unicorn {
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Color")
    private String color;
    @JsonProperty("Gender")
    private String gender;

    public Unicorn() {
    }

    public Unicorn(final String name, final String color, final String gender) {
        this.name = name;
        this.color = color;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }
}
