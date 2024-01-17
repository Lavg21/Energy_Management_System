package com.ems.emsdevice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


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
