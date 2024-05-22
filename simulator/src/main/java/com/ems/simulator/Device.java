package com.ems.simulator;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "consumption", nullable = false, length = 100)
    private Double consumption;
}
