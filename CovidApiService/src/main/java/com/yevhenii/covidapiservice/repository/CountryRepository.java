package com.yevhenii.covidapiservice.repository;

import com.yevhenii.covidapiservice.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country getByCode(String code);
}
