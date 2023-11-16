package com.petlover.petsocial.controller;

import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.model.entity.ExStatus;
import com.petlover.petsocial.model.entity.Exchange;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.service.ApplyService;
import com.petlover.petsocial.service.ExchangeService;
import com.petlover.petsocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplyService applyService;

    //Create exchange

    //sua
    @PostMapping("/create")
    public ResponseEntity<?> createExchange (@RequestHeader("Authorization") String jwt, @RequestBody CreateExchangeDTO createExchangeDTO) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        if(userDTO!=null){
            PetDTO pet =  createExchangeDTO.getPetDTO();
            List<Exchange> exchanges = exchangeService.getAllExchange(userDTO);
            boolean isDuplicate = false;

            for (Exchange exchange : exchanges) {
                if (exchange.getPet().getId().equals(pet.getId()) && !exchange.getStatus().equals(ExStatus.REMOVED)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (isDuplicate){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Duplicated pet.");
            }else{
                ExchangeDTO exchange = exchangeService.addExchange(userDTO, createExchangeDTO.getPetDTO().getId(), createExchangeDTO.getPaymentAmount());
                return ResponseEntity.ok(exchange);
            }

        }else{
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create exchange.");
        }
    }

    //Update status Exchange to Completed when have apply have status true
    @PutMapping("/{id}/completed")
    public ResponseEntity<?> updateCompletedExchange(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        ExchangeDTO updatedExchange = exchangeService.updateExchange(userDTO, id);
        if (updatedExchange != null) {
            return ResponseEntity.ok(updatedExchange);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange not found.");
        }
    }

    //Update payment amout for exchange
    @PutMapping("/{id}/edit-cash")
    public ResponseEntity<?> editCashExchange(@RequestHeader("Authorization") String jwt, @PathVariable Long id, @RequestParam("paymentAmount") int paymentAmount) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        ExchangeDTO updatedExchange = exchangeService.editCashExchange(userDTO, id, paymentAmount);
        if (updatedExchange != null) {
            return ResponseEntity.ok(updatedExchange);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange not found.");
        }
    }

    //Remove exchange
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExchange(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        ExchangeDTO deExchange = exchangeService.deleteExchange(userDTO, id);
        if (deExchange != null) {
            return ResponseEntity.ok(deExchange);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange not found.");
        }
    }

    // view exchange one
    @GetMapping("/{id}")
    public ResponseEntity<?> getExchange(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        ExchangeDTO exchange = exchangeService.getOneExchangeDTO(userDTO, id);
        if (exchange != null) {
            return ResponseEntity.ok(exchange);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange not found.");
        }
    }

    @GetMapping("/{userid}/{id}")
    public ResponseEntity<?> getExchangeWithOthers(@PathVariable Long userid, @PathVariable Long id) throws UserException {
        UserDTO userDTO1 = userService.findUserProfileById(userid);
        ExchangeDTO exchange = exchangeService.getOneExchangeDTO(userDTO1, id);
        if (exchange != null) {
            return ResponseEntity.ok(exchange);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exchange not found.");
        }
    }


    @GetMapping("/view-exchange")
    public ResponseEntity<?> listExchanges(@RequestHeader("Authorization") String jwt) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ExchangeDTO> exchanges = exchangeService.getAllExchangeDTO(userDTO);
        if (!exchanges.isEmpty()) {
            return ResponseEntity.ok(exchanges);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No exchanges found.");
        }
    }

    @GetMapping("/{id}/view-applies")
    public ResponseEntity<?> listAppliesWithExchange(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ApplyDTO> applyDTOS = applyService.getApplyForExchange(userDTO, id);
        if (!applyDTOS.isEmpty()) {
            return ResponseEntity.ok(applyDTOS);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No apply found.");
        }
    }

    @GetMapping("/getAllExchange")
    public ResponseEntity<?> getAllExchangToShowInMarketPlacce(@RequestHeader("Authorization") String jwt) throws UserException {
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<ExchangeDTO> exchanges = exchangeService.getAllExchangeToShow(userDTO);
        System.out.println("co gi o day khong"+exchanges);
        if (!exchanges.isEmpty()) {
            return ResponseEntity.ok(exchanges);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No exchanges found.");
        }
    }


}
