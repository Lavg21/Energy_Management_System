package com.ems.emsmonitoring.service;

import com.ems.emsmonitoring.domain.dto.DeviceMessageDTO;
import com.ems.emsmonitoring.domain.dto.MonitoringHourlyDataDTO;
import com.ems.emsmonitoring.domain.dto.MonitoringMaxDataDTO;
import com.ems.emsmonitoring.domain.entity.MonitoringHourlyData;
import com.ems.emsmonitoring.domain.entity.MonitoringMaxData;
import com.ems.emsmonitoring.exception.DeviceIdException;
import com.ems.emsmonitoring.exception.MonitoringDataServiceException;
import com.ems.emsmonitoring.repository.MonitoringHourlyDataRepository;
import com.ems.emsmonitoring.repository.MonitoringMaxDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class MonitoringDataService {

    @Autowired
    private final MonitoringHourlyDataRepository monitoringHourlyDataRepository;

    @Autowired
    private final MonitoringMaxDataRepository monitoringMaxDataRepository;

    private final String QUEUE_NAME = "monitoring";

    @PostConstruct  // This method (postConstruct()) is called after the class is constructed.
    @Async
    // This allows the postConstruct() method to run asynchronously, meaning the normal execution is not interrupted when a message is received.
    public void postConstruct() {
        receiveMessageFromDevice();
    }

    public void createMonitoringMaxData(MonitoringMaxDataDTO monitoringMaxDataDTO) {
        MonitoringMaxData monitoringMaxData = convertToMaxEntity(monitoringMaxDataDTO);

        if (monitoringMaxDataDTO.getDeviceId() == null) {
            throw new DeviceIdException("The id of the device cannot be null!");
        } else {
            monitoringMaxDataRepository.save(monitoringMaxData);
        }
    }

    public void deleteMonitoringData(Integer deviceId) {
        MonitoringHourlyData monitoringHourlyData = monitoringHourlyDataRepository
                .findByDeviceId(deviceId)
                .orElseThrow(() -> new DeviceIdException("The device with ID" + deviceId + "was not found!"));

        monitoringHourlyDataRepository.delete(monitoringHourlyData);
    }

    private MonitoringHourlyDataDTO convertToDTO(MonitoringHourlyData monitoringHourlyData) {
        return MonitoringHourlyDataDTO.builder()
                .deviceId(monitoringHourlyData.getDeviceId())
                .hourlyConsumption(monitoringHourlyData.getHourlyConsumption())
                .build();
    }

    private MonitoringMaxData convertToMaxEntity(MonitoringMaxDataDTO monitoringMaxDataDTO) {
        return MonitoringMaxData.builder()
                .deviceId(monitoringMaxDataDTO.getDeviceId())
                .maxConsumption(monitoringMaxDataDTO.getMaxConsumption())
                .build();
    }

    private void receiveMessageFromDevice() {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages from device backend...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                DeviceMessageDTO deviceMessage = deserializeString(message);

                if (deviceMessage.getAction().equals("post")) {
                    MonitoringMaxDataDTO monitoringMaxDataDTO = new MonitoringMaxDataDTO(deviceMessage.getDeviceId(), deviceMessage.getMaxConsumption());
                    createMonitoringMaxData(monitoringMaxDataDTO);
                } else {
                    deleteMonitoringData(deviceMessage.getDeviceId());
                }

                System.out.println(" [x] Received: " + deviceMessage);
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException exception) {
            throw new MonitoringDataServiceException("There was an error when receiving the message from device");
        }
    }

    private static DeviceMessageDTO deserializeString(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(string, DeviceMessageDTO.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error when processing JSON! (possible invalid structure or invalid values for attributes)");
            return null;
        }
    }

    // ProcessBuilder processBuilder = new ProcessBuilder("python", "C:\\blabla\\hello.py");
}
