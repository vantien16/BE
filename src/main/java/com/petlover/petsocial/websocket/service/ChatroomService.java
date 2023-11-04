package com.petlover.petsocial.websocket.service;

import com.petlover.petsocial.websocket.domain.Chatroom;
import com.petlover.petsocial.websocket.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatroomService {
    @Autowired
    ChatroomRepository chatroomRepository;


    public Chatroom findChatroomBySenderIdAndRecipientId(Long senderId, Long recipientId){
        Optional<Chatroom> found = chatroomRepository.findChatroomBySenderIdAndRecipientId(senderId, recipientId);
        if(found.isPresent()){
            return found.get();
        }
        return null;
    }
}
