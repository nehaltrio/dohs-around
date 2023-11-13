package com.ecom.dohsaround.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.dohsaround.model.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
