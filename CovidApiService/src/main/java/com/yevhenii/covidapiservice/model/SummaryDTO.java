package com.yevhenii.covidapiservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class SummaryDTO {
    @JsonProperty("Date")
    private LocalDate date;
    @JsonProperty("Countries")
    private List<CountrySummaryDTO> countries;

    @Data
    public static class CountrySummaryDTO {
        @JsonProperty("Country")
        private String name;
        @JsonProperty("Slug")
        private String slug;
        @JsonProperty("ISO2")
        private String iso2;
        @JsonProperty("NewConfirmed")
        private int newConfirmed;
    }
}
