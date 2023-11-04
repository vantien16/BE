package com.petlover.petsocial.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserForAdminDTO {
    private Long  id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String role;
    private boolean enable;
}
