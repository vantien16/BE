package com.petlover.petsocial.controller;


import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.model.entity.Apply;
import com.petlover.petsocial.model.entity.Exchange;
import com.petlover.petsocial.payload.request.ApplyDTO;
import com.petlover.petsocial.payload.request.UserDTO;
import com.petlover.petsocial.service.ApplyService;
import com.petlover.petsocial.service.ExchangeService;
import com.petlover.petsocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplyService applyService;

    @PostMapping("/create")
    public ResponseEntity<?> createApply (@RequestHeader("Authorization") String jwt, @RequestParam Long userid, @RequestParam Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        UserDTO userDTO1 = userService.findUserProfileById(userid);
        Exchange exchange = exchangeService.getOneExchange(userDTO1, id);
        if (exchange != null) {
            Apply apply = applyService.createApply(exchange,userDTO, userDTO1);
            return ResponseEntity.ok("Apply created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange not found.");
        }
    }

    @PutMapping("/{eid}/{aid}")
    public ResponseEntity<?> updateStatus(@RequestHeader("Authorization") String jwt, @PathVariable("eid") Long eid, @PathVariable("aid") Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        Apply apply = applyService.updateApply(userDTO, id, eid);
        if (apply!=null){
            ApplyDTO applyDTO = ApplyDTO.convertToDTO(apply);
            return ResponseEntity.ok(applyDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apply not found.");
        }
    }

    @GetMapping("/view-applies")
    public ResponseEntity<?> viewApplies(@RequestHeader("Authorization") String jwt) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ApplyDTO> applyDTOS = applyService.getApplyForUser(userDTO);
        if(!applyDTOS.isEmpty()){
            return ResponseEntity.ok(applyDTOS);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Do not have any applies.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeApply(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        Apply apply = applyService.removeApply(id, userDTO);
        if (apply!=null){
            ApplyDTO applyDTO = ApplyDTO.convertToDTO(apply);
            return ResponseEntity.ok(applyDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apply not found.");
        }
    }



}
