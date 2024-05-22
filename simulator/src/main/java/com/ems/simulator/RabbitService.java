package com.ems.simulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitService {

    private final String QUEUE_NAME = "simulation";

    @Value("${rabbit-config.ip}")
    private String rabbitHost;

    @Value("${rabbit-config.port}")
    private Integer rabbitPort;

    public void sendMessageToMonitoring(Integer deviceId, Double consumption) {
        // create connection to the server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // the queue we send messages to
            // the message content is byte array
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DeviceMessageDTO deviceInformation = new DeviceMessageDTO(deviceId, consumption);

            String message = serializeObject(deviceInformation);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        } catch(IOException | TimeoutException exception) {
            System.err.println("There was an error when sending messages to queue!");
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
