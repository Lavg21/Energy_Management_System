package com.ems.emsmonitoring.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MonitoringMaxDataDTO {

    private Integer deviceId;
    private Double maxConsumption;
}
