package com.ems.emsdevice.repository;

import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.domain.entity.UserAvailable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {

    List<Device> findAllByUserAvailable(UserAvailable userAvailable);
}
