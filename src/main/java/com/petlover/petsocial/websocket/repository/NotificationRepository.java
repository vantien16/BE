package com.petlover.petsocial.websocket.repository;

import com.petlover.petsocial.websocket.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("FROM Notification n WHERE n.recId = :recId")
    List<Notification> getNotificationOfUser(Long recId);
}
