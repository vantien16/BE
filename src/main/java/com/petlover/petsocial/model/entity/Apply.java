package com.petlover.petsocial.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="Apply")
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date apply_date;
    private ApplyStatus status;

    @ManyToOne
    @JoinColumn(name = "exchange_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Exchange exchange;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;


}
