package com.petlover.petsocial.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="Chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "message", columnDefinition = "nvarchar(200)")
    private String message;
    private Date send_date;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Exchange exchange;


    @ManyToOne
    @JoinColumn(name = "sender_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
}
