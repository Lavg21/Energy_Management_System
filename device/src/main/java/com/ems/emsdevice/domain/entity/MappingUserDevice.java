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
    //@JoinColumn(name = "device_id", insertable = false, updatable = false)
    private Device device;

    @Column(name = "count")
    private Integer numberOfDevices;
}
