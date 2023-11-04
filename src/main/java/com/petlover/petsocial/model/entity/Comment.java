package com.petlover.petsocial.model.entity;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="Comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "react_id")
    @JsonIgnore
    @ToString.Exclude
    private Reaction react;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    @ToString.Exclude
    private Post post;

    @Column
    private LocalDateTime createdTime;


}
