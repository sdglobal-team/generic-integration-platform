package com.generic.integration.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.generic.integration.model.City;

public interface CityRepository extends ArangoRepository<City, String> {
}
