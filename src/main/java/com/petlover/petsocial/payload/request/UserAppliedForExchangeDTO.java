package com.petlover.petsocial.payload.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAppliedForExchangeDTO {
  private Long id;
  private String name;
  private String email;
  private String phone;
  private String avatar;
}
