package com.petlover.petsocial.websocket.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "notification")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id")
    private Long notiId;

    @Column(name="rec_id")
    private Long recId;

    @Column(name="rec_name")
    private String recName;

    @Column
    private String content;

    @Column(name = "created_on",nullable = false)
    private Date createdOn;

    @Column(name="status")
    private String status;
}
