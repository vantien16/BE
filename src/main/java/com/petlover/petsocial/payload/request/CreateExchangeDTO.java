package com.petlover.petsocial.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateExchangeDTO {
    private PetDTO petDTO;
    private String description;
    private int paymentAmount;
}