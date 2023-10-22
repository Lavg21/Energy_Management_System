package com.ems.emsdevice.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MappingUserDeviceDTO {

    private Integer id;
    private Integer userID;
    private Integer deviceID;
    private Integer numberOfDevices;
}
