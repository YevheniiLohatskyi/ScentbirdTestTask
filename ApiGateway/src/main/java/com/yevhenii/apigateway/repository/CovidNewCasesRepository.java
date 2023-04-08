package com.yevhenii.apigateway.repository;

import com.yevhenii.apigateway.model.Country;
import com.yevhenii.apigateway.model.CovidCountryStatistics;
import com.yevhenii.apigateway.model.CovidNewCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CovidNewCasesRepository extends JpaRepository<CovidNewCases, Long> {

    @Query("SELECT new com.yevhenii.apigateway.model.CovidCountryStatistics(max(c.newCases), min(c.newCases)) " +
           "FROM CovidNewCases c " +
           "WHERE c.country = :country " +
           "AND c.date BETWEEN :fromDate AND :toDate")
    CovidCountryStatistics getCountryStatsByDateBetween(@Param("country") Country country,
                                                        @Param("fromDate") LocalDate fromDate,
                                                        @Param("toDate") LocalDate toDate);
}
