package com.ems.emsmonitoring.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class DeviceMaxMessageDTO {

    private Integer deviceId;
    private Double maxConsumption;
    private String action;
}
