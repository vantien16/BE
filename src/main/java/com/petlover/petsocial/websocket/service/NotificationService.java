package com.petlover.petsocial.websocket.service;

import com.petlover.petsocial.websocket.domain.Notification;
import com.petlover.petsocial.websocket.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> findNotificationOfUser(Long recId){
        return notificationRepository.getNotificationOfUser(recId);
    }
}
