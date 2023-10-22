package com.ems.emsdevice.service;

import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.domain.entity.MappingUserDevice;
import com.ems.emsdevice.exception.MappingNotFoundException;
import com.ems.emsdevice.repository.MappingUserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MappingUserDeviceService {

    @Autowired
    private final MappingUserDeviceRepository mappingUserDeviceRepository;

    @Autowired
    private final DeviceService deviceService;

    public MappingUserDevice createMapping(MappingUserDevice mappingUserDevice, Integer deviceID) {

        Device device = deviceService.findDeviceById(deviceID);

        mappingUserDevice.setDevice(device);

        return mappingUserDeviceRepository.save(mappingUserDevice);
    }

    public MappingUserDevice findMappingById(Integer id) {

        return mappingUserDeviceRepository.findById(id).orElse(null);
    }

    public void deleteMapping(Integer id) {

        MappingUserDevice mappingUserDevice = mappingUserDeviceRepository
                .findById(id)
                .orElseThrow(() -> new MappingNotFoundException("Mapping with ID " + id + " not found"));

        mappingUserDeviceRepository.delete(mappingUserDevice);
    }

    public List<MappingUserDevice> getAllMappings() {

        return mappingUserDeviceRepository.findAll();
    }
}
