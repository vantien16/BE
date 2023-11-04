package com.petlover.petsocial.service;

import com.petlover.petsocial.model.entity.Apply;
import com.petlover.petsocial.model.entity.Exchange;
import com.petlover.petsocial.payload.request.ApplyDTO;
import com.petlover.petsocial.payload.request.UserDTO;

import java.util.List;

public interface ApplyService {

    public Apply createApply(Exchange exchange, UserDTO userDTO, UserDTO userDTOown);
    public Apply updateApply(UserDTO userDTO, Long applyid, Long exchangeid);
    //public Apply deleteApply(UserDTO userDTO, int applyid);
    public List<ApplyDTO> getApplyForExchange(UserDTO userDTO, Long id);
    public List<ApplyDTO> getApplyForUser(UserDTO userDTO);
    public Apply getApplyById(Long appId);
    public Apply removeApply(Long appid, UserDTO userDTO);


}
