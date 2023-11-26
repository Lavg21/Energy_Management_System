package com.ems.emsmonitoring.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "monitoring_hourly_data")
public class MonitoringHourlyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "hourly_consumption", nullable = false)
    private Double hourlyConsumption;

    @Column(name = "device_id", nullable = false, unique = true)
    private Integer deviceId;
}
