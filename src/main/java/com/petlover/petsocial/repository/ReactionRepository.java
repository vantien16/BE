package com.petlover.petsocial.repository;


import com.petlover.petsocial.model.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
     @Query("SELECT r FROM Reaction r WHERE r.user.id = :idUser and r.post.id= :idPost")
    Reaction isReactionExist(@Param("idUser") Long idUser,@Param("idPost") Long idPost);
    @Query("SELECT r FROM Reaction r WHERE r.post.id= :idPost")
     List<Reaction> findByPostId(@Param("idPost") Long idPost);
     List<Reaction> findAll();
    void deleteById(Long id);
}
