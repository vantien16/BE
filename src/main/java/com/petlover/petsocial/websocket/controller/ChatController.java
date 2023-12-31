package com.petlover.petsocial.websocket.controller;

import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.websocket.config.WebSocketEventListener;
import com.petlover.petsocial.websocket.domain.Chatroom;
import com.petlover.petsocial.websocket.domain.Message;
import com.petlover.petsocial.websocket.dto.NotificationDto;
import com.petlover.petsocial.websocket.repository.ChatroomRepository;
import com.petlover.petsocial.websocket.repository.MessageRepository;
import com.petlover.petsocial.websocket.utils.MapperUtils;
import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatController {
    @Autowired
    WebSocketEventListener auth;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @MessageMapping("/chat")
    public void sendMessage(Message chatMessage
            , SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        headerAccessor.setSessionId(sessionId);

        Optional<Chatroom> chatroom = chatroomRepository.findChatroomBySenderIdAndRecipientId(chatMessage.getSenderId(),chatMessage.getRecipientId());
        String chatroomId = "";
        if(chatroom.isEmpty()){
            chatroomId= String.format("%s_%s", chatMessage.getSenderId(), chatMessage.getRecipientId());

            Chatroom senderRecipient = Chatroom
                    .builder()
                    .chatroomId(chatroomId)
                    .senderId(chatMessage.getSenderId())
                    .recipientId(chatMessage.getRecipientId())
                    .build();

            Chatroom recipientSender = Chatroom
                    .builder()
                    .chatroomId(chatroomId)
                    .senderId(chatMessage.getRecipientId())
                    .recipientId(chatMessage.getSenderId())
                    .build();
            try{
                chatroomRepository.save(senderRecipient);
                chatroomRepository.save(recipientSender);
            }
            catch(Exception ex){
                ex.printStackTrace();
                throw new InternalException("Cannont create new chat room between sender "+chatMessage.getSenderId()+" and recipient "+chatMessage.getRecipientId());
            }

        }
        else{
            chatroomId = chatroom.get().getChatroomId();
        }
        chatMessage.setChatroomId(chatroomId);
        Message saved = null;
        try{
            saved = messageRepository.save(chatMessage);
        }
        catch(Exception ex){
            throw new InternalException("Cannot create new message in chatroomId "+ chatroomId);
        }

        NotificationDto noti = MapperUtils.mapperObject(saved, NotificationDto.class);

        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientName(),"/queue/messages",noti);
    }

}
