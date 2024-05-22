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

            DeviceDTO createdDeviceDTO = deviceService.convertToDTO(createdDevice);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdDeviceDTO);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("/mapping")
    public ResponseEntity<?> createMapping(@RequestBody MappingDTO mappingDTO) {
        try {
            Device createdMapping = deviceService.createMapping(mappingDTO);

            DeviceDTO createdMappingDTO = deviceService.convertToDTO(createdMapping);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdMappingDTO);
        } catch (DeviceServiceException exception) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();

        if (devices != null && !devices.isEmpty()) {
            List<DeviceDTO> deviceDTOs = deviceService.getDeviceDTOS(devices);

            return ResponseEntity.ok(deviceDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/unmapped")
    public ResponseEntity<List<DeviceDTO>> getAllUnmappedDevices() {
        List<Device> devices = deviceService.getAllUnmappedDevices();

        if (devices != null && !devices.isEmpty()) {
            List<DeviceDTO> deviceDTOs = deviceService.getDeviceDTOS(devices);

            return ResponseEntity.ok(deviceDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findDeviceById(@PathVariable Integer id) {
        Device device = deviceService.findDeviceById(id);

        if (device != null) {

            DeviceDTO deviceDTO = deviceService.convertToDTO(device);

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

    @GetMapping("/user/all")
    public ResponseEntity<?> getAllMappings() {
        try {
            return ResponseEntity.ok(deviceService.getAllMappings());
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("Exception occurred while retrieving all mappings!");
        }
    }
}
