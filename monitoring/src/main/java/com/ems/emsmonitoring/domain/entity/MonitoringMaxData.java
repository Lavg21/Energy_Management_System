package com.ems.emsmonitoring.domain.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "monitoring_max_data")
public class MonitoringMaxData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "max_consumption", nullable = false)
    private Double maxConsumption;

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;
}
