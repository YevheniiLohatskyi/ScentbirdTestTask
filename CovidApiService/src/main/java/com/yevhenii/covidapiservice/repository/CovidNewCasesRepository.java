package com.yevhenii.covidapiservice.repository;

import com.yevhenii.covidapiservice.model.CovidNewCases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CovidNewCasesRepository extends JpaRepository<CovidNewCases, Long> {

}
