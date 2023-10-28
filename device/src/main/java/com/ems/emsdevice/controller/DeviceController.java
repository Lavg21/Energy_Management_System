package com.ems.emsdevice.controller;

import com.ems.emsdevice.domain.dto.DeviceDTO;
import com.ems.emsdevice.domain.dto.MappingDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.exception.DeviceNotFoundException;
import com.ems.emsdevice.exception.DeviceServiceException;
import com.ems.emsdevice.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<?> createDevice(@RequestBody DeviceDTO deviceDTO) {
        try {
            Device createdDevice = deviceService.createDevice(deviceDTO);

            DeviceDTO createdDeviceDTO = DeviceDTO.builder()
                    .id(createdDevice.getId())
                    .address(createdDevice.getAddress())
                    .description(createdDevice.getDescription())
                    .consumption(createdDevice.getConsumption())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(createdDeviceDTO);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/mapping")
    public ResponseEntity<?> createMapping(@RequestBody MappingDTO mappingDTO) {
        try {
            Device createdMapping = deviceService.createMapping(mappingDTO);

            DeviceDTO createdMappingDTO = DeviceDTO.builder()
                    .id(createdMapping.getId())
                    .address(createdMapping.getAddress())
                    .description(createdMapping.getDescription())
                    .consumption(createdMapping.getConsumption())
                    .userAvailable(createdMapping.getUserAvailable())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(createdMappingDTO);
        } catch (DeviceServiceException exception) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();

        if (devices != null && !devices.isEmpty()) {
            List<DeviceDTO> deviceDTOs = devices.stream()
                    .map(device -> DeviceDTO.builder()
                            .id(device.getId())
                            .address(device.getAddress())
                            .description(device.getDescription())
                            .consumption(device.getConsumption())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(deviceDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findDeviceById(@PathVariable Integer id) {
        Device device = deviceService.findDeviceById(id);

        if (device != null) {

            DeviceDTO deviceDTO = DeviceDTO.builder()
                    .id(device.getId())
                    .address(device.getAddress())
                    .description(device.getDescription())
                    .consumption(device.getConsumption())
                    .build();

            return ResponseEntity.ok(deviceDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Device with ID %d not found!", id));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable Integer id, @RequestBody DeviceDTO deviceDTO) {
        try {
            DeviceDTO updatedDeviceDTO = deviceService.updateDevice(id, deviceDTO);

            return ResponseEntity.ok(updatedDeviceDTO);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Device with ID %d not found!", id));
        }
    }

    @DeleteMapping("/mapping/{deviceId}")
    public ResponseEntity<?> deleteMapping(@PathVariable Integer deviceId) {

        try {
            deviceService.deleteMapping(deviceId);

            return ResponseEntity.ok("Mapping for device with ID " + deviceId + " has been deleted!");
        } catch (DeviceServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable Integer id) {
        try {
            deviceService.deleteDevice(id);

            return ResponseEntity.ok("Device with ID " + id + " has been deleted!");
        } catch (DeviceServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllDevicesForUserId(@PathVariable Integer userId) {
        try {
            List<DeviceDTO> deviceDTOList = deviceService.getAllDevicesForUserId(userId);
            return ResponseEntity.ok(deviceDTOList);
        } catch (DeviceServiceException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
