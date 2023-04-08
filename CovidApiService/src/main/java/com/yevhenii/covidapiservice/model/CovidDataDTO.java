package com.yevhenii.covidapiservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CovidDataDTO {
    @JsonProperty("Country")
    private String country;

    @JsonProperty("Cases")
    private int cases;

    @JsonProperty("Date")
    private LocalDateTime date;
}
