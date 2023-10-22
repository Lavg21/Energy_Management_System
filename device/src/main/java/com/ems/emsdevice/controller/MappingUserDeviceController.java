package com.ems.emsdevice.controller;

import com.ems.emsdevice.domain.dto.MappingUserDeviceDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.domain.entity.MappingUserDevice;
import com.ems.emsdevice.service.MappingUserDeviceService;
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
@RequestMapping(value = "/mapping")
public class MappingUserDeviceController {

    @Autowired
    private final MappingUserDeviceService mappingUserDeviceService;

    @PostMapping
    public ResponseEntity<MappingUserDeviceDTO> createMapping(@RequestBody MappingUserDeviceDTO mappingUserDeviceDTO) {

        MappingUserDevice mappingUserDevice = MappingUserDevice.builder()
                .userID(mappingUserDeviceDTO.getUserID())

                .numberOfDevices(mappingUserDeviceDTO.getNumberOfDevices())
                .build();

        MappingUserDevice createdMapping = mappingUserDeviceService.createMapping(mappingUserDevice, mappingUserDeviceDTO.getDeviceID());

        MappingUserDeviceDTO createdMappingDTO = MappingUserDeviceDTO.builder()
                .id(createdMapping.getId())
                .userID(createdMapping.getUserID())
                .deviceID(createdMapping.getDevice().getId())
                .numberOfDevices(createdMapping.getNumberOfDevices())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMappingDTO);
    }

    @GetMapping
    public ResponseEntity<List<MappingUserDeviceDTO>> getAllMappings() {

        List<MappingUserDevice> mappingUserDevices = mappingUserDeviceService.getAllMappings();

        if (mappingUserDevices != null && !mappingUserDevices.isEmpty()) {

            List<MappingUserDeviceDTO> mappingDTO = mappingUserDevices.stream()
                    .map(mapping -> MappingUserDeviceDTO.builder()
                            .id(mapping.getId())
                            .userID(mapping.getUserID())
                            .deviceID(mapping.getDevice().getId())
                            .numberOfDevices(mapping.getNumberOfDevices())
                            .build())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(mappingDTO);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMapping(@PathVariable Integer id) {

        MappingUserDevice mappingUserDevice = mappingUserDeviceService.findMappingById(id);

        if (mappingUserDevice != null) {
            mappingUserDeviceService.deleteMapping(id);

            return ResponseEntity.ok("Mapping with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
