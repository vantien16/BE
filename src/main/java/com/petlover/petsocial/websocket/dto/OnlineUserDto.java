package com.petlover.petsocial.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDto {
    private Long id;
    private String sessionId;
    private String name;
    private Integer noOfNewMessages;
    private String status;


}
