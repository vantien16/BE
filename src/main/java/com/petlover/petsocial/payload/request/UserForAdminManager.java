package com.petlover.petsocial.payload.request;

import com.petlover.petsocial.model.entity.AuthenticationProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserForAdminManager {
    private Long  id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String role;
    private BigDecimal balance;
    private boolean enable;
    private AuthenticationProvider authProvider;
    private int totalPet;
    private int totalPost;
}
