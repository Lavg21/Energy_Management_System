package com.ems.emsdevice.service;

import com.ems.emsdevice.domain.dto.DeviceDTO;
import com.ems.emsdevice.domain.dto.MappingDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.domain.entity.UserAvailable;
import com.ems.emsdevice.exception.DeviceNotFoundException;
import com.ems.emsdevice.exception.DeviceServiceException;
import com.ems.emsdevice.repository.DeviceRepository;
import com.ems.emsdevice.repository.UserAvailableRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    @Autowired
    private final DeviceRepository deviceRepository;

    @Autowired
    private final UserAvailableRepository userAvailableRepository;

    public Device createDevice(DeviceDTO deviceDTO) {
        Device device = Device.builder()
                .address(deviceDTO.getAddress())
                .description(deviceDTO.getDescription())
                .consumption(deviceDTO.getConsumption())
                .build();

        if (StringUtils.isBlank(device.getAddress()) || StringUtils.isBlank(device.getDescription())
                || device.getConsumption() <= 0)
            throw new IllegalArgumentException("Invalid data for device!");
        else {
            return deviceRepository.save(device);
        }
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
        Device device = deviceRepository
                .findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device with ID " + id + " not found"));

        deviceRepository.delete(device);
    }

    public Device createMapping(MappingDTO mappingDTO) {
        Device foundDevice = deviceRepository.findById(mappingDTO.getDeviceID()).orElse(null);

        Optional<UserAvailable> userAvailableOptional = userAvailableRepository.findById(mappingDTO.getUserID());
        if (userAvailableOptional.isEmpty()) {
            throw new DeviceServiceException("The user was not found!");
        }

        UserAvailable userAvailable = userAvailableOptional.get();

        if (foundDevice != null) {
            foundDevice.setUserAvailable(userAvailable);

            return deviceRepository.save(foundDevice);
        } else {
            throw new DeviceServiceException("The device was not found!");
        }
    }

    public void deleteMapping(Integer deviceId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        if (deviceOptional.isPresent()) {
            Device foundDevice = deviceOptional.get();
            foundDevice.setUserAvailable(null);
            deviceRepository.save(foundDevice);
        } else {
            throw new DeviceServiceException("The device was not found!");
        }
    }

    public List<DeviceDTO> getAllDevicesForUserId(Integer userId) {
        Optional<UserAvailable> userAvailableOptional = userAvailableRepository.findById(userId);
        if (userAvailableOptional.isEmpty()) {
            throw new DeviceServiceException("The user was not found!");
        }
        UserAvailable userAvailable = userAvailableOptional.get();
        List<Device> deviceList = deviceRepository.findAllByUserAvailable(userAvailable);

        return deviceList.stream().map(x -> {

            return DeviceDTO.builder()
                    .id(x.getId())
                    .address(x.getAddress())
                    .consumption(x.getConsumption())
                    .description(x.getDescription())
                    .userAvailable(x.getUserAvailable())
                    .build();
        }).toList();
    }

    public List<MappingDTO> getAllMappings() {
        List<Device> deviceList = deviceRepository.findAll();
        List<MappingDTO> mappingDTOList = new ArrayList<>();

        deviceList.stream()
                .filter(x -> x.getUserAvailable() != null)
                .forEach(x -> mappingDTOList.add(new MappingDTO(x.getUserAvailable().getId(), x.getId())));

        return mappingDTOList;
    }

    public List<Device> getAllUnmappedDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream().filter(x -> x.getUserAvailable() == null).collect(Collectors.toList());
    }
}
