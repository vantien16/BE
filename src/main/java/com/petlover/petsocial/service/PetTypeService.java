package com.petlover.petsocial.service;

import com.petlover.petsocial.model.entity.Pet_Type;
import com.petlover.petsocial.payload.request.PetTypeDTO;

import java.util.List;

public interface PetTypeService {
    List<PetTypeDTO> getAllTypePet();
    Pet_Type getPetType(Long id);
}
