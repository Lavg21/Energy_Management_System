package com.ems.emsmonitoring.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class DeviceMessageDTO {

    private Integer deviceId;
    private Double consumption;
}
