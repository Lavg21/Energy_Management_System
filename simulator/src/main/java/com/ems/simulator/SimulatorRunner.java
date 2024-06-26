package com.ems.simulator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class SimulatorRunner implements ApplicationRunner {

    private final DeviceRepository deviceRepository;

    private final RabbitService rabbitService;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("App started....");
        List<Device> deviceList = deviceRepository.findAll();

        for (Device d : deviceList) {
            CompletableFuture.runAsync(() -> {
                runSimulation(d.getId());   // We will run this function asynchronously
            });
        }
    }

    private void runSimulation(Integer deviceId) {
        Integer lineNumber = 0;

        BufferedReader br;
        InputStream inputStream = SimulatorRunner.class.getResourceAsStream("/sensor.csv");
        if (inputStream == null) {
            System.err.println("Cannot create input stream for file sensor.csv!");
            return;
        }

        br = new BufferedReader(new InputStreamReader(inputStream));

        while (true) {
            System.out.println(lineNumber);

            String line;
            try {
                line = br.readLine();
                if (line == null) {
                    System.err.println("Reading from file is done! Terminating simulation!");
                    return;
                }

                Double consumption = Double.parseDouble(line.strip());
                System.out.printf("%d sends: %f%n", deviceId, consumption);

                // Sending message
                rabbitService.sendMessageToMonitoring(deviceId, consumption);
            } catch (IOException e) {
                System.err.println("Error while reading from file, line: " + lineNumber);
                return;
            }

            try {
                Thread.sleep(1000);   // 10 minutes = 1000 milliseconds * 60 * 10
            } catch (InterruptedException e) {
                System.err.println("Interruption occurred while running simulation!");
                return;
            }

            lineNumber++;
        }
    }
}
