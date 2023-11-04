package com.petlover.petsocial.repository;

import com.petlover.petsocial.model.entity.Pet_Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeRepository extends JpaRepository<Pet_Type, Long> {
   Pet_Type getById(Long id);

}
