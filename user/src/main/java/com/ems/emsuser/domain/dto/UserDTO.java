package com.ems.emsuser.domain.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id;
    private String name;
    private String email;
    private String password;
    private boolean admin;
}
