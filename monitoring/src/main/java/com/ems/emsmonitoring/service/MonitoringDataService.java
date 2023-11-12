package com.ems.emsmonitoring.service;

import com.ems.emsmonitoring.domain.dto.DeviceMessageDTO;
import com.ems.emsmonitoring.domain.dto.MonitoringDataDTO;
import com.ems.emsmonitoring.domain.entity.MonitoringData;
import com.ems.emsmonitoring.exception.DeviceIdException;
import com.ems.emsmonitoring.exception.MonitoringDataServiceException;
import com.ems.emsmonitoring.repository.MonitoringDataRepository;
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
    private final MonitoringDataRepository monitoringDataRepository;
    private final String QUEUE_NAME = "monitoring";

    @PostConstruct  // This method (postConstruct()) is called after the class is constructed.
    @Async
    // This allows the postConstruct() method to run asynchronously, meaning the normal execution is not interrupted when a message is received.
    public void postConstruct() {
        receiveMessageFromDevice();
    }

    public void createMonitoringData(MonitoringDataDTO monitoringDataDTO) {
        MonitoringData monitoringData = convertToEntity(monitoringDataDTO);

        if (monitoringDataDTO.getDeviceId() == null) {
            throw new DeviceIdException("The id of the device cannot be null!");
        } else {
            monitoringDataRepository.save(monitoringData);
        }
    }

    public void deleteMonitoringData(Integer deviceId) {
        MonitoringData monitoringData = monitoringDataRepository
                .findByDeviceId(deviceId)
                .orElseThrow(() -> new DeviceIdException("The device with ID" + deviceId + "was not found!"));

        monitoringDataRepository.delete(monitoringData);
    }

    private MonitoringDataDTO convertToDTO(MonitoringData monitoringData) {
        return MonitoringDataDTO.builder()
                .deviceId(monitoringData.getDeviceId())
                .consumption(monitoringData.getConsumption())
                .build();
    }

    private MonitoringData convertToEntity(MonitoringDataDTO monitoringDataDTO) {
        return MonitoringData.builder()
                .deviceId(monitoringDataDTO.getDeviceId())
                .consumption(monitoringDataDTO.getConsumption())
                .build();
    }

    private void receiveMessageFromDevice() {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                DeviceMessageDTO deviceMessage = deserializeString(message);

                if (deviceMessage.getAction().equals("post")) {
                    MonitoringDataDTO monitoringDataDTO = new MonitoringDataDTO(deviceMessage.getDeviceId(), 0.0);
                    createMonitoringData(monitoringDataDTO);
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
