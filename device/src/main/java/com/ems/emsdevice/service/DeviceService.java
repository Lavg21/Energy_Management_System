package com.ems.emsdevice.service;

import com.ems.emsdevice.domain.dto.DeviceDTO;
import com.ems.emsdevice.domain.dto.DeviceMessageDTO;
import com.ems.emsdevice.domain.dto.MappingDTO;
import com.ems.emsdevice.domain.entity.Device;
import com.ems.emsdevice.domain.entity.UserAvailable;
import com.ems.emsdevice.exception.DeviceNotFoundException;
import com.ems.emsdevice.exception.DeviceServiceException;
import com.ems.emsdevice.repository.DeviceRepository;
import com.ems.emsdevice.repository.UserAvailableRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    @Autowired
    private final DeviceRepository deviceRepository;

    @Autowired
    private final UserAvailableRepository userAvailableRepository;

    private final String QUEUE_NAME = "monitoring";

    public DeviceDTO convertToDTO(Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .address(device.getAddress())
                .description(device.getDescription())
                .consumption(device.getConsumption())
                .build();
    }

    private Device convertToEntity(DeviceDTO deviceDTO) {
        return Device.builder()
                .address(deviceDTO.getAddress())
                .description(deviceDTO.getDescription())
                .consumption(deviceDTO.getConsumption())
                .build();
    }

    public List<DeviceDTO> getDeviceDTOS(List<Device> devices) {
        return devices.stream()
                .map(device -> DeviceDTO.builder()
                        .id(device.getId())
                        .address(device.getAddress())
                        .description(device.getDescription())
                        .consumption(device.getConsumption())
                        .build())
                .collect(Collectors.toList());
    }

    public Device createDevice(DeviceDTO deviceDTO) {
        Device device = convertToEntity(deviceDTO);

        if (StringUtils.isBlank(device.getAddress()) || StringUtils.isBlank(device.getDescription())
                || device.getConsumption() <= 0)
            throw new IllegalArgumentException("Invalid data for device!");
        else {

            Device savedDevice = deviceRepository.save(device);

            sendMessageToMonitoring(device.getId(), "post");

            return savedDevice;
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

        return convertToDTO(updatedDevice);
    }

    public void deleteDevice(Integer id) {
        Device device = deviceRepository
                .findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device with ID " + id + " not found"));

        sendMessageToMonitoring(device.getId(), "delete");

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

            return convertToDTO(x);
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

    private void sendMessageToMonitoring(Integer deviceId, String action) {
        // create connection to the server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // the queue we send messages to
            // the message content is byte array
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DeviceMessageDTO deviceInformation = new DeviceMessageDTO(deviceId, action);
            String message = serializeObject(deviceInformation);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch(IOException | TimeoutException exception) {
            throw new DeviceServiceException("There was an error when sending messages to queue!");
        }
    }

    private String serializeObject(DeviceMessageDTO deviceMessage) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(deviceMessage);
        } catch (JsonProcessingException e) {
            System.err.println("Could not serialize object into JSON string!");
            return null;
        }
    }
}
