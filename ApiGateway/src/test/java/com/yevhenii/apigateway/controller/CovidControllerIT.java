package com.yevhenii.apigateway.controller;

import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.service.CovidStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CovidController.class)
public class CovidControllerIT {

    @TestConfiguration
    static class CovidControllerITContextConfiguration {
        @Bean
        public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
            return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CovidStatsService covidStatsService;

    private Map<String, CovidCountryStatistics> testStatistics;

    @BeforeEach
    public void setUp() {
        CovidCountryStatistics testStats1 = new CovidCountryStatistics(100, 10);
        CovidCountryStatistics testStats2 = new CovidCountryStatistics(200, 20);

        testStatistics = new HashMap<>();
        testStatistics.put("Country1", testStats1);
        testStatistics.put("Country2", testStats2);
    }

    @Test
    public void getNewCasesStats_returnsStatisticsByCountry() throws Exception {
        List<String> countries = Arrays.asList("Country1", "Country2");
        LocalDate fromDate = LocalDate.of(2023, 4, 1);
        LocalDate toDate = LocalDate.of(2023, 4, 2);

        when(covidStatsService.getStatisticsByCountry(countries, fromDate, toDate)).thenReturn(testStatistics);

        mockMvc.perform(get("/new-cases-stats")
                .param("countries", String.join(",", countries))
                .param("from", fromDate.toString())
                .param("to", toDate.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.Country1.maxNewCases").value(100))
            .andExpect(jsonPath("$.Country1.minNewCases").value(10))
            .andExpect(jsonPath("$.Country2.maxNewCases").value(200))
            .andExpect(jsonPath("$.Country2.minNewCases").value(20));
    }
}