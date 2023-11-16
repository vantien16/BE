package com.petlover.petsocial.service;

import com.petlover.petsocial.exception.PetException;
import com.petlover.petsocial.payload.request.*;

import java.util.List;

public interface PetService {
    PetDTO insertPet(CreatePetDTO createPetDTO, UserDTO userDTO) throws PetException;
    List<PetDTO> getAllPet(UserDTO userDTO);
    PetDTO deletePet(Long id,UserDTO userDTO) throws PetException;
    PetDTO getOnePet(Long id, UserDTO userDTO);
    PetDTO updatePet(Long id,PetUpdateDTO petUpdateDTO, UserDTO userDTO);
    List<PetToPostDTO> getAllPetPost(UserDTO userDTO);
}
