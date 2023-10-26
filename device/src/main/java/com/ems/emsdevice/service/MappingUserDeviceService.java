package com.ems.emsdevice.service;

import com.ems.emsdevice.domain.dto.UserDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.domain.entity.MappingUserDevice;
import com.ems.emsdevice.exception.ClientException;
import com.ems.emsdevice.exception.ClientStatusException;
import com.ems.emsdevice.exception.UserAvailableNotFoundException;
import com.ems.emsdevice.repository.MappingUserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MappingUserDeviceService {

    @Autowired
    private final MappingUserDeviceRepository mappingUserDeviceRepository;

    @Autowired
    private final DeviceService deviceService;


    public MappingUserDevice createMapping(MappingUserDevice mappingUserDevice, Integer userID, Integer deviceID, String token) {

        Device device = deviceService.findDeviceById(deviceID);
        getUserFromEndpoint(userID, token);

        mappingUserDevice.setUserID(userID);
        mappingUserDevice.setDevice(device);

        return mappingUserDeviceRepository.save(mappingUserDevice);
    }

    public MappingUserDevice findMappingById(Integer id) {

        return mappingUserDeviceRepository.findById(id).orElse(null);
    }

    public void deleteMapping(Integer id) {

        MappingUserDevice mappingUserDevice = mappingUserDeviceRepository
                .findById(id)
                .orElseThrow(() -> new UserAvailableNotFoundException("Mapping with ID " + id + " not found"));

        mappingUserDeviceRepository.delete(mappingUserDevice);
    }

    public List<MappingUserDevice> getAllMappings() {

        return mappingUserDeviceRepository.findAll();
    }

    private void getUserFromEndpoint(Integer userID, String token) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", token);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        try {

            ResponseEntity<UserDTO> responseEntity = restTemplate.exchange("http://localhost:8080/user/" + userID, HttpMethod.GET, httpEntity, UserDTO.class);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new ClientException("The userID was not found!");
            } else {
                throw new ClientStatusException("Could not retrieve userID!");
            }

        } catch (HttpServerErrorException e) {
            throw new ClientStatusException("Could not retrieve userID!");
        }
    }
}
