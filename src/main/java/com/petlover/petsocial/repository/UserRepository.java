package com.petlover.petsocial.repository;

import com.petlover.petsocial.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM user u WHERE u.email = :email", nativeQuery = true)
    User findByEmail(@Param("email") String email);




    User findByVerificationCode(String code);
    User findByResetPasswordToken(String token);

    User getById(Long id);

    @Query(value="SELECT * FROM user u where u.role <> \"ROLE_ADMIN\"",nativeQuery = true)
    List<User> listUser();

    @Query (value="SELECT * FROM user u where u.enable=1", nativeQuery = true)
    List<User> listUserHome();
    Optional<User> findByName(String name);

    @Query(value="SELECT * FROM user u where u.role <> \"ROLE_ADMIN\" and u.name like CONCAT('%',%?1%,'%') ",nativeQuery = true)
    List<User> searchUserForAdmin(String name);
}
