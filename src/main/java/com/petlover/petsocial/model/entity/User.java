package com.petlover.petsocial.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

@Table(name="User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", columnDefinition = "nvarchar(255)")
    private String name;
    @Column(name = "email", columnDefinition = "nvarchar(255)", unique = true)
    private String email;
    @Column(name = "password", columnDefinition = "nvarchar(255)")
    private String password;
    @Column(name = "phone", columnDefinition = "nvarchar(255)")
    private String phone;
    @Column(name = "avatar", columnDefinition = "nvarchar(1111)")
    private String avatar;
    @Column(name = "role", columnDefinition = "nvarchar(100)")
    private String role;
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @Column(name = "verification_code")
    private String verificationCode;
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Pet> pets;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Comment> comments;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Reaction> reactions;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Exchange> exchanges;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Apply> applies;
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;
}
