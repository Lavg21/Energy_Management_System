package com.ems.emsdevice.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeviceDTO {

    private Integer id;
    private String description;
    private String address;
    private Double consumption;
}
