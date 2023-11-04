package com.petlover.petsocial.websocket.controller;

import com.petlover.petsocial.service.UserService;
import com.petlover.petsocial.websocket.config.WebSocketEventListener;
import com.petlover.petsocial.websocket.dto.OnlineUserDto;
import com.petlover.petsocial.websocket.service.MessageService;
import com.petlover.petsocial.websocket.utils.MapperUtils;
import com.sun.jdi.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ws/users")
public class UserWSController {
    @Autowired
    WebSocketEventListener webSocketEventListener;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;


    @GetMapping("/{currentUserId}")
    public ResponseEntity<List<OnlineUserDto>> getOnlineUsers(@PathVariable Long currentUserId) {
        List<OnlineUserDto>usersWithStatus = new ArrayList<>();


        //List<OnlineUserDto>offlineUsers = MapperUtils.mapperList(userService.getAllUsers());
        List<OnlineUserDto>offlineUsers = MapperUtils.mapperList(userService.getAllUsers(), OnlineUserDto.class);

        offlineUsers.stream().map(u->{
            u.setStatus("OFFLINE");
            return u;
        }).collect(Collectors.toList());

        try{
            Set<OnlineUserDto> onlsSet = webSocketEventListener.getOnlineUsrs();
            if(onlsSet!=null){
                List<OnlineUserDto>onls = onlsSet.stream().collect(Collectors.toList());
                onls.forEach(o->{
                    int count = messageService.countNewMessagesFromOnlineUser(currentUserId, o.getUserId());
                    o.setNoOfNewMessages(count);
                    o.setStatus("ONLINE");
                });
                usersWithStatus.addAll(onls);
                List<OnlineUserDto> finalOnls = onls;
                offlineUsers.forEach(u->{
                    if(finalOnls.stream().map(OnlineUserDto::getName).collect(Collectors.toList()).contains(u.getName())==false){
                        usersWithStatus.add(u);
                    }
                });
            }
            else{
                usersWithStatus.addAll(offlineUsers);
            }

        }
        catch(Exception ex){
            throw new InternalException("Cannot get the number of online users");
        }
        return ResponseEntity.ok(usersWithStatus);
    }
}
