package com.petlover.petsocial.repository;

import com.petlover.petsocial.model.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
   @Query(value="Select * From pet p WHERE p.user_id = %?1% and p.status = 1 ORDER BY p.id DESC",nativeQuery = true)
    List<Pet> getAllByIdUser(Long id);
    Pet getById(Long id);
    @Query(value="Select * From pet p WHERE p.pet_type_id = %?1% and p.status = 1",nativeQuery = true)
    List<Pet> getAllByPetType(Long id);

    @Query(value="Select * From pet p",nativeQuery = true)
    List<Pet> getAllPetForAdmin();

    @Query(value="Select * From pet p WHERE p.status =1",nativeQuery = true)
    List<Pet> getAllPetDisplayForAdmin();

}
