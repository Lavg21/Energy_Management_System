package com.ems.emsdevice.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users_available")
public class UserAvailable {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
}
