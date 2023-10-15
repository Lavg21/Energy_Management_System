package com.ems.emsdevice.service;

import com.ems.emsdevice.domain.dto.DeviceDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.exception.DeviceNotFoundException;
import com.ems.emsdevice.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    @Autowired
    private final DeviceRepository deviceRepository;

    public Device createDevice(Device device) {

        return deviceRepository.save(device);
    }

    public List<Device> getAllDevices() {

        return deviceRepository.findAll();
    }

    public Device findDeviceById(Integer id) {

        return deviceRepository.findById(id).orElse(null);
    }

    public DeviceDTO updateDevice(Integer id, DeviceDTO deviceDTO) {

        Device existingDevice = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device with ID " + id + " not found"));


        existingDevice.setAddress(deviceDTO.getAddress());
        existingDevice.setConsumption(deviceDTO.getConsumption());
        existingDevice.setDescription(deviceDTO.getDescription());

        Device updatedDevice = deviceRepository.save(existingDevice);

        return DeviceDTO.builder()
                .id(updatedDevice.getId())
                .address(updatedDevice.getAddress())
                .description(updatedDevice.getDescription())
                .consumption(updatedDevice.getConsumption())
                .build();
    }

    public void deleteDevice(Integer id) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device with ID " + id + " not found"));

        deviceRepository.delete(device);
    }

}
