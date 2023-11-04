package com.petlover.petsocial.serviceImp;


import com.petlover.petsocial.model.entity.Pet_Type;
import com.petlover.petsocial.payload.request.PetTypeDTO;
import com.petlover.petsocial.repository.PetRepository;
import com.petlover.petsocial.repository.PetTypeRepository;
import com.petlover.petsocial.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetTypeServiceImp implements PetTypeService {
    @Autowired
    private PetTypeRepository petTypeRepository;
    @Autowired
    private PetRepository petRepository;
    @Override
    public List<PetTypeDTO> getAllTypePet() {
        List<Pet_Type> listPet =  petTypeRepository.findAll();
        List<PetTypeDTO> listPetDTO = new ArrayList<>();
        for(Pet_Type pet_type : listPet)
        {
            PetTypeDTO petTypeDTO = new PetTypeDTO();
            petTypeDTO.setName(pet_type.getName());
            petTypeDTO.setId(pet_type.getId());
            listPetDTO.add(petTypeDTO);
        }
        return listPetDTO;
    }
    @Override
    public Pet_Type getPetType(Long id) {
        return petTypeRepository.getById(id);
    }

}
