package com.petlover.petsocial.repository;


import com.petlover.petsocial.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value="Select * From post p WHERE p.enable = 1 and p.status = 1 ORDER BY p.id DESC",nativeQuery = true)
    List<Post> getAll();
    @Query(value="Select * From post p WHERE p.enable = 1 and p.status = 1 and p.user_id=%?1% ORDER BY p.id DESC",nativeQuery = true)
    List<Post> getAllYourPost(Long id);
    @Query(value="Select * From post p WHERE p.enable = 1 and p.status = 1 and p.content like CONCAT('%',%?1%,'%') ",nativeQuery = true)
    List<Post> searchPost(String content);
    Post getById(Long id);
    @Query(value="Select * From post p WHERE p.enable = 0 and p.status = 1 ORDER BY p.id DESC",nativeQuery = true)
    List<Post> getAllPostDisable();
    @Query(value="Select * From post p",nativeQuery = true)
    List<Post> getAllPostForAdmin();

    @Query(value="Select * From post p WHERE p.status = 0",nativeQuery = true)
    List<Post> getAllPostDeleteForAdmin();
    @Query(value="Select * From post p WHERE p.status = 1",nativeQuery = true)
    List<Post> getAllPostDisplayUserForAdmin();
}
