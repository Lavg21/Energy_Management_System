package com.ems.emsmonitoring.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MonitoringHourlyDataDTO {

    private Integer deviceId;
    private Double hourlyConsumption;
}
