package com.ems.emsmonitoring.repository;

import com.ems.emsmonitoring.domain.entity.MonitoringMaxData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoringMaxDataRepository extends JpaRepository<MonitoringMaxData, Integer> {

    Optional<MonitoringMaxData> findByDeviceId(Integer deviceId);
}
