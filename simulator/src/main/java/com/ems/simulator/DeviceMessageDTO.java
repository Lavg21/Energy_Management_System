package com.ems.simulator;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeviceMessageDTO {

    private Integer deviceId;
    private Double consumption;
}