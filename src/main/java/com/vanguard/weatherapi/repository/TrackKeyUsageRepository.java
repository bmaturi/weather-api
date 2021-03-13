package com.vanguard.weatherapi.repository;

import com.vanguard.weatherapi.entity.TrackKeyUsage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackKeyUsageRepository extends JpaRepository<TrackKeyUsage, Long> {

}
