package com.yevhenii.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CovidCountryStatistics {
    private int maxNewCases;
    private int minNewCases;
}
