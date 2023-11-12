package com.ems.emsmonitoring.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MonitoringDataDTO {

    private Integer deviceId;
    private Double consumption;
}
