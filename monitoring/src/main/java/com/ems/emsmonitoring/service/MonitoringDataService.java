package com.ems.emsmonitoring.service;

import com.ems.emsmonitoring.domain.dto.DeviceMaxMessageDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class MonitoringDataService {

    @Autowired
    private final MonitoringHourlyDataRepository monitoringHourlyDataRepository;

    @Autowired
    private final MonitoringMaxDataRepository monitoringMaxDataRepository;

    private Map<Integer, List<Double>> simulationMap = new TreeMap<>();

    private final String QUEUE_NAME_DEVICE = "monitoring";

    private final String QUEUE_NAME_SIMULATION = "simulation";

    @Value("${rabbit-config.ip}")
    private String rabbitHost;

    @Value("${rabbit-config.port}")
    private Integer rabbitPort;

    @PostConstruct  // This method (postConstruct()) is called after the class is constructed.
    @Async
    // This allows the postConstruct() method to run asynchronously, meaning the normal execution is not interrupted when a message is received.
    public void postConstruct() {
        receiveMessageFromDevice();
        receiveMessageFromSimulation();
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
        Optional<MonitoringHourlyData> monitoringHourlyData = monitoringHourlyDataRepository
                .findByDeviceId(deviceId);

        monitoringHourlyData.ifPresent(monitoringHourlyDataRepository::delete);

        Optional<MonitoringMaxData> monitoringMaxData = monitoringMaxDataRepository
                .findByDeviceId(deviceId);

        monitoringMaxData.ifPresent(monitoringMaxDataRepository::delete);
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

    private void saveOrUpdateHourlyConsumption(MonitoringHourlyData data) {
        Optional<MonitoringHourlyData> foundDataOptional = monitoringHourlyDataRepository.findByDeviceId(data.getDeviceId());
        if (foundDataOptional.isPresent()) {
            MonitoringHourlyData foundData = foundDataOptional.get();
            foundData.setHourlyConsumption(data.getHourlyConsumption());
            monitoringHourlyDataRepository.save(foundData);
        } else {
            monitoringHourlyDataRepository.save(data);
        }
    }

    private void receiveMessageFromDevice() {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            //factory.setHost("localhost");
            factory.setHost(rabbitHost);
            factory.setPort(rabbitPort);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME_DEVICE, false, false, false, null);
            System.out.println(" [*] Waiting for messages from device backend...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                DeviceMaxMessageDTO deviceMessage = deserializeStringMax(message);

                if (deviceMessage.getAction().equals("post")) {
                    MonitoringMaxDataDTO monitoringMaxDataDTO = new MonitoringMaxDataDTO(deviceMessage.getDeviceId(), deviceMessage.getMaxConsumption());
                    createMonitoringMaxData(monitoringMaxDataDTO);
                } else {
                    deleteMonitoringData(deviceMessage.getDeviceId());
                }

                System.out.println(" [x] Received: " + deviceMessage);
            };
            channel.basicConsume(QUEUE_NAME_DEVICE, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException exception) {
            throw new MonitoringDataServiceException("There was an error when receiving the message from device");
        }
    }

    private void receiveMessageFromSimulation() {
        try {
            ConnectionFactory factory = new ConnectionFactory();

            //factory.setHost("localhost");
            factory.setHost(rabbitHost);
            factory.setPort(rabbitPort);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME_SIMULATION, false, false, false, null);
            System.out.println(" [*] Waiting for messages from simulation...");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");

                DeviceMessageDTO deviceMessage = deserializeString(message);
                Integer deviceId = deviceMessage.getDeviceId();

                if (simulationMap.containsKey(deviceId)) {
                    List<Double> currentConsumptions = simulationMap.get(deviceId);
                    currentConsumptions.add(deviceMessage.getConsumption());
                    if (currentConsumptions.size() == 6) {  // Once every 6 readings (meaning 1 hour) we save to DB
                        Double average = currentConsumptions.stream().mapToDouble(Double::doubleValue).sum() / 6;

                        MonitoringHourlyData monitoringHourlyData = MonitoringHourlyData.builder()
                                .deviceId(deviceId)
                                .hourlyConsumption(average)
                                .build();

                        saveOrUpdateHourlyConsumption(monitoringHourlyData);

                        currentConsumptions = new ArrayList<>();    // Reset the list
                        simulationMap.put(deviceId, currentConsumptions);
                    } else {
                        simulationMap.put(deviceId, currentConsumptions);   // Add to the list
                    }

                } else {
                    simulationMap.put(deviceId, new ArrayList<>());
                }

                System.out.println(" [x] Received: " + deviceMessage);
            };
            channel.basicConsume(QUEUE_NAME_SIMULATION, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException exception) {
            throw new MonitoringDataServiceException("There was an error when receiving the message from device");
        }
    }

    private static DeviceMaxMessageDTO deserializeStringMax(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(string, DeviceMaxMessageDTO.class);
        } catch (JsonProcessingException e) {
            System.err.println("Error when processing JSON! (possible invalid structure or invalid values for attributes)");
            return null;
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
}
