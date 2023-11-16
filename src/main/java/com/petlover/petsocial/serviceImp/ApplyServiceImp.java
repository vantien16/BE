package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.ApplyRepository;
import com.petlover.petsocial.repository.ExchangeRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.ApplyService;
import com.petlover.petsocial.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApplyServiceImp implements ApplyService {



    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplyRepository applyRepository;


    //sua
    @Override
    public Apply createApply(Exchange exchange, UserDTO userDTO, UserDTO userDTOown) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        User userown = userRepository.findById(userDTOown.getId()).orElse(null);
        Exchange exchange1 = exchangeRepository.findById(exchange.getId()).orElse(null);
        if (exchange1 != null && exchange1.getUser().getId()==userown.getId()){

            Apply apply = new Apply();
            apply.setExchange(exchange1);
            apply.setApply_date(new Date());
            apply.setUser(user);
            apply.setStatus(ApplyStatus.PENDING);
            applyRepository.save(apply);
            return apply;
        }else {
            return null;
        }
    }

    @Override
    public Apply updateApply(UserDTO userDTO, Long applyid, Long exchangeid) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        Exchange exchange = exchangeRepository.findById(exchangeid).orElse(null);
        if(exchange!=null && user!=null){
            if(user.getExchanges().contains(exchange)){
                for (Apply app : exchange.getApplies() ) {
                    if(app.getId()==applyid){
                        app.setStatus(ApplyStatus.COMPLETED);
                        applyRepository.save(app);
                    }else{
                        app.setStatus(ApplyStatus.UNCOMPLETED);
                        applyRepository.save(app);
                    }
                }
                return applyRepository.findById(applyid).orElse(null);
            }
        }
        return null;
    }

    @Override
    public List<ApplyDTO> getApplyForExchange(UserDTO userDTO, Long id) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        List<ApplyDTO> applyDTOS = new ArrayList<>();
        if (exchange!=null && user.getExchanges().contains(exchange)){
             for (Apply app : exchange.getApplies()){
                 if(app.getStatus()!=ApplyStatus.REMOVED){
                     ApplyDTO applyDTO = ApplyDTO.convertToDTO(app);
                     applyDTOS.add(applyDTO);
                 }
             }
             return applyDTOS;

        }else {
            return null;
        }

    }

    @Override
    public List<ApplyDTO> getApplyForUser(UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userDTO.getId());
        List<ApplyDTO> appliesDTO = new ArrayList<>();
        if(user.isPresent()){
            List<Apply> applies = user.get().getApplies();
            for ( Apply app: applies) {
                if(app.getStatus()!=ApplyStatus.REMOVED){
                    ApplyDTO applyDTO = ApplyDTO.convertToDTO(app);
                    appliesDTO.add(applyDTO);
                }

            }
        }
        return appliesDTO;
    }



    @Override
    public Apply getApplyById(Long appId) {
        return applyRepository.findById(appId).orElse(null);
    }

    @Override
    public Apply removeApply(Long appid, UserDTO userDTO) {
        Optional<User> user = userRepository.findById(userDTO.getId());
        if(user.isPresent()){
            for (Apply apply : user.get().getApplies()){
                if(apply.getId()==appid){
                    apply.setStatus(ApplyStatus.REMOVED);
                    applyRepository.save(apply);
                }
            }
            return applyRepository.findById(appid).orElse(null);
        }
        return null;
    }




}
