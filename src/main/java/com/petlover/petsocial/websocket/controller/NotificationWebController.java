package com.petlover.petsocial.websocket.controller;

import com.petlover.petsocial.websocket.domain.Notification;
import com.petlover.petsocial.websocket.repository.NotificationRepository;
import com.petlover.petsocial.websocket.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ws/notification")
public class NotificationWebController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{recId}")
    public ResponseEntity<List<Notification>> getAndSeenNotification (@PathVariable Long recId){
        List<Notification> notificationList = notificationService.findNotificationOfUser(recId);
        notificationService.updateStatus(notificationList);
        return new ResponseEntity<List<Notification>>(notificationList, HttpStatus.OK);
    }
    @GetMapping("/noti/{recId}")
    public ResponseEntity<List<Notification>> getAllNotification (@PathVariable Long recId){
        List<Notification> notificationList = notificationService.findNotificationOfUser(recId);
//        notificationService.updateStatus(notificationList);
        return new ResponseEntity<List<Notification>>(notificationList, HttpStatus.OK);
    }
}
