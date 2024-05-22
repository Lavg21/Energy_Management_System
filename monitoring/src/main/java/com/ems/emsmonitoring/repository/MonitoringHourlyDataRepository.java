package com.ems.emsmonitoring.repository;

import com.ems.emsmonitoring.domain.entity.MonitoringHourlyData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoringHourlyDataRepository extends JpaRepository<MonitoringHourlyData, Integer> {

    Optional<MonitoringHourlyData> findByDeviceId(Integer deviceId);
}
