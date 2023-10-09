package com.ems.emsdevice.repository;

import com.ems.emsdevice.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
}
