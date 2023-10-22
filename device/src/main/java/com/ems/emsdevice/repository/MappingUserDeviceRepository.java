package com.ems.emsdevice.repository;

import com.ems.emsdevice.domain.entity.MappingUserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MappingUserDeviceRepository extends JpaRepository<MappingUserDevice, Integer> {
}
