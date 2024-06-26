package com.ems.emsdevice.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeviceMessageDTO {

    private Integer deviceId;
    private Double maxConsumption;
    private String action;
}
