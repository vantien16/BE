package com.petlover.petsocial.serviceImp;


import com.petlover.petsocial.exception.PetException;
import com.petlover.petsocial.model.entity.Pet;
import com.petlover.petsocial.model.entity.Pet_Type;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.PetRepository;
import com.petlover.petsocial.repository.PetTypeRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CloudinaryService;
import com.petlover.petsocial.service.PetService;
import com.petlover.petsocial.service.PetTypeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class PetServiceImp implements PetService {
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    PetTypeRepository petTypeRepository;
    @Autowired
    PetTypeService petTypeService;
    @Autowired
    PetRepository petRepository;
    @Autowired
    HttpSession session;
    @Autowired
    private UserRepository userRepo;
    @Override
    public PetDTO insertPet(CreatePetDTO createPetDTO, UserDTO userDTO) throws PetException {

            Pet_Type pet_type = petTypeService.getPetType(createPetDTO.getIdPetType());
            System.out.println(pet_type);

        if(createPetDTO.getFile()==null){
            throw new PetException("pet not found with image");
        }
        if(createPetDTO.getName().isEmpty()){
            throw new PetException("pet not found with name");
        }
        if(createPetDTO.getName()==null){
            throw new PetException("pet not found with name");
        }
        if(createPetDTO.getDescription().equals("")){
            throw new PetException("pet not found with description");
        }
        if(createPetDTO.getDescription()==null){
            throw new PetException("pet not found with description");
        }
        Pet newPet = new Pet();
        try {
            String image = cloudinaryService.uploadFile(createPetDTO.getFile());
            newPet.setImage(image);
        }catch(Exception e){
            throw new PetException("pet not found with image");
        }

            newPet.setName(createPetDTO.getName());
            newPet.setDescription(createPetDTO.getDescription());
            User user = userRepo.getById(userDTO.getId());
            newPet.setUser(user);
            newPet.setPet_type(pet_type);
            newPet.setStatus(true);
            petRepository.save(newPet);
            return new PetDTO(newPet.getId(), newPet.getImage(),newPet.getName(),newPet.getDescription());

    }
    @Override
    public List<PetDTO> getAllPet(UserDTO userDTO)
    {
          List<Pet> petList= petRepository.getAllByIdUser(userDTO.getId());
          List<PetDTO> listpetDTO = new ArrayList<>();
          for(Pet pet : petList) {
              PetDTO petDTO = new PetDTO();
              petDTO.setId(pet.getId());
              petDTO.setName(pet.getName());
              petDTO.setDescription(pet.getDescription());
              petDTO.setImage(pet.getImage());
              listpetDTO.add(petDTO);
        }
        return listpetDTO;
    }
    public List<PetToPostDTO> getAllPetPost()
    {
        User user = (User) session.getAttribute("user");
        List<Pet> petList= petRepository.getAllByIdUser(user.getId());
        List<PetToPostDTO> listpetDTO = new ArrayList<>();
        for(Pet pet : petList) {
            PetToPostDTO petDTO = new PetToPostDTO();
            petDTO.setId(pet.getId());
            petDTO.setName(pet.getName());
            petDTO.setImage(pet.getImage());
            listpetDTO.add(petDTO);
        }
        return listpetDTO;
    }
    @Override
    public PetDTO deletePet(Long id,UserDTO userDTO) throws PetException {

        Pet getPet = petRepository.getById(id);
        if(getPet==null) {
            return null;
        }
        if(getPet.getUser().getId() == userDTO.getId()) {
            getPet.setStatus(false);
        }
        else {
            throw new PetException("You not delete pet another");
        }
        petRepository.save(getPet);
        return new PetDTO(getPet.getId(),getPet.getImage(), getPet.getName(),getPet.getDescription());
    }
    @Override
    public PetDTO getOnePet(Long id, UserDTO userDTO)
    {

        Pet getPet = petRepository.getById(id);
        if(getPet.getUser().getId() == userDTO.getId()) {
            return new PetDTO(getPet.getId(), getPet.getImage(), getPet.getName(), getPet.getDescription());
        }
        return null;

    }
    @Override
    public PetDTO updatePet(Long id,PetUpdateDTO petUpdateDTO, UserDTO userDTO)
    {

        Pet getPet = petRepository.getById(id);
        if(getPet==null){
            return null;
        }
        if(getPet.getUser().getId() != userDTO.getId()) {
            return null;
        }

        if(petUpdateDTO.getFile()==null){
            if(petUpdateDTO.getName()!=null) {
                if(petUpdateDTO.getName().equals("")){
                    return null;
                }
                getPet.setName(petUpdateDTO.getName());
            }
            if(petUpdateDTO.getDescription()!=null) {
                if(petUpdateDTO.getDescription().equals("")){
                    return null;
                }
                getPet.setDescription(petUpdateDTO.getDescription());
            }
        }else{
            try {
                String image = cloudinaryService.uploadFile(petUpdateDTO.getFile());
                getPet.setImage(image);
            }catch (Exception e){

            }
            if(petUpdateDTO.getName()!=null) {
                if(petUpdateDTO.getName().equals("")){
                    return null;
                }
                getPet.setName(petUpdateDTO.getName());
            }
            if(petUpdateDTO.getDescription()!=null) {
                if(petUpdateDTO.getDescription().equals("")){
                    return null;
                }
                getPet.setDescription(petUpdateDTO.getDescription());
            }
        }
        petRepository.save(getPet);
        return new PetDTO(getPet.getId(),getPet.getImage(), getPet.getName(),getPet.getDescription());

    }


}
