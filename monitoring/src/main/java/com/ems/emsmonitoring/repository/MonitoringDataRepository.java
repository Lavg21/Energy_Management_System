package com.ems.emsmonitoring.repository;

import com.ems.emsmonitoring.domain.entity.MonitoringData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoringDataRepository extends JpaRepository<MonitoringData, Integer> {

    Optional<MonitoringData> findByDeviceId(Integer deviceId);
}
