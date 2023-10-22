package com.ems.emsdevice.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_devices")
public class MappingUserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userID;

    @ManyToOne(cascade = CascadeType.ALL)
    private Device device;

    @Column(name = "count")
    private Integer numberOfDevices;
}
