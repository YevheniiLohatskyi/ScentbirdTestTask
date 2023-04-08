package com.yevhenii.apigateway.repository;

import com.yevhenii.apigateway.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country getByName(String countryName);
}
