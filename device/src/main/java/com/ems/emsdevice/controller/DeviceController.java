package com.ems.emsdevice.controller;

import com.ems.emsdevice.domain.dto.DeviceDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.exception.DeviceNotFoundException;
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

    @PostMapping("/create")
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO deviceDTO) {

        Device device = Device.builder()
                .address(deviceDTO.getAddress())
                .description(deviceDTO.getDescription())
                .consumption(deviceDTO.getConsumption())
                .build();

        Device createdDevice = deviceService.createDevice(device);

        DeviceDTO createdDeviceDTO = DeviceDTO.builder()
                .id(createdDevice.getId())
                .address(createdDevice.getAddress())
                .description(createdDevice.getDescription())
                .consumption(createdDevice.getConsumption())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeviceDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {

        List<Device> users = deviceService.getAllDevices();

        if (users != null && !users.isEmpty()) {
            List<DeviceDTO> deviceDTOs = users.stream()
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
    public ResponseEntity<DeviceDTO> findUserById(@PathVariable Integer id) {
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
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateUser(@PathVariable Integer id, @RequestBody DeviceDTO deviceDTO) {

        try {
            DeviceDTO updatedUserDTO = deviceService.updateDevice(id, deviceDTO);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (DeviceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDevice(@PathVariable Integer id) {

        Device device = deviceService.findDeviceById(id);

        if (device != null) {
            deviceService.deleteDevice(id);
            return ResponseEntity.ok("Device with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
