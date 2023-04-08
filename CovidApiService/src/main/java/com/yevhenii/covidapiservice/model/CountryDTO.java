package com.yevhenii.covidapiservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CountryDTO {
    @JsonProperty("Country")
    private String country;

    @JsonProperty("Slug")
    private String slug;

    @JsonProperty("ISO2")
    private String iso2;
}
