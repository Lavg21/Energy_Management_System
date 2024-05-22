package com.ems.emschat.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {

    private Integer id;

    private String sender;

    private String receiver;

    private String messageText;

    private Boolean seen;
}
