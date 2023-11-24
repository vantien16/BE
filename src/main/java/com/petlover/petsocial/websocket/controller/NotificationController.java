package com.petlover.petsocial.websocket.controller;

import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.websocket.config.WebSocketEventListener;
import com.petlover.petsocial.websocket.domain.Message;
import com.petlover.petsocial.websocket.domain.Notification;
import com.petlover.petsocial.websocket.repository.ChatroomRepository;
import com.petlover.petsocial.websocket.repository.MessageRepository;
import com.petlover.petsocial.websocket.repository.NotificationRepository;
import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    @Autowired
    WebSocketEventListener auth;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @MessageMapping("/notification")
    public void sendNotification(@Payload Notification notification){
        Notification saved = null;
        try{
            saved = notificationRepository.save(notification);
        }
        catch(Exception ex){
            throw new InternalException("Error");
        }
        messagingTemplate.convertAndSendToUser(notification.getRecName(),"/queue/noti",notification);

    }


}
