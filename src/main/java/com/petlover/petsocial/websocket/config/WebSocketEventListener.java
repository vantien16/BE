package com.petlover.petsocial.websocket.config;

import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.websocket.dto.OnlineUserDto;
import com.petlover.petsocial.websocket.utils.MapperUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public class   WebSocketEventListener {
    private Set<OnlineUserDto> onlineUsrs = new HashSet<>();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        @SuppressWarnings("rawtypes")
        GenericMessage connectHeader = (GenericMessage) stompAccessor
                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        // to the server
        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

        String login = nativeHeaders.get("username").get(0);
        //System.out.println(login);
        String sessionId = stompAccessor.getSessionId();
        //System.out.println(sessionId);
        if(this.onlineUsrs==null){
            this.onlineUsrs = new HashSet<>();
        }
        Optional<User> usr = userRepository.findByName(login);
        if(usr.isPresent()){
            OnlineUserDto onl = MapperUtils.mapperObject(usr.get(), OnlineUserDto.class);
            onl.setSessionId(sessionId);
            this.onlineUsrs.add(onl);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompAccessor.getSessionId();
        //System.out.println(sessionId);
        this.onlineUsrs.removeIf(user -> user.getSessionId().equals(sessionId));

    }

}
