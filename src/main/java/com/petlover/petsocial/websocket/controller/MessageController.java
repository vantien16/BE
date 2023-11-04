package com.petlover.petsocial.websocket.controller;

import com.petlover.petsocial.websocket.domain.Chatroom;
import com.petlover.petsocial.websocket.domain.Message;
import com.petlover.petsocial.websocket.service.ChatroomService;
import com.petlover.petsocial.websocket.service.MessageService;
import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/ws/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatroomService chatroomService;


    @GetMapping("/{senderId}/{recipientId}")
    public ResponseEntity<List<Message>> getChatMessages(@PathVariable Long senderId,
                                                         @PathVariable Long recipientId) {
        List<Message> messagesFromSenderRepicient = null;
        try{
            List<Message> msgs = messageService.findChatMessagesFromSelectedUser(senderId, recipientId);
            messageService.updateMessagesStatusToDelivered(msgs);

            Chatroom cr = chatroomService.findChatroomBySenderIdAndRecipientId(senderId, recipientId);

            if(cr!=null){
                messagesFromSenderRepicient = messageService.findChatMessagesByChatroomId(cr.getChatroomId());
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new InternalException("Cannot find messages between sender "+senderId+" and recipient "+recipientId);
        }
        return new ResponseEntity<List<Message>>(messagesFromSenderRepicient, HttpStatus.OK);
    }

}
