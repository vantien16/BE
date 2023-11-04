package com.petlover.petsocial.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateDTO {
    private String name;
    private String phone;
    private MultipartFile file;

}
