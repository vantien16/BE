package com.petlover.petsocial.controller;

import com.petlover.petsocial.exception.PetException;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.payload.response.ResponseData;
import com.petlover.petsocial.service.PetService;
import com.petlover.petsocial.service.PetTypeService;
import com.petlover.petsocial.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {


    @Autowired
    PetTypeService petTypeService;
    @Autowired
    PetService petService;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpSession session;
    @Autowired
    private UserService userService;
    @GetMapping("/createpet")
    public ResponseEntity<?> createTypePet(){
        ResponseData responseData = new ResponseData();
        List<PetTypeDTO> list = petTypeService.getAllTypePet();
        request.setAttribute("listTypePet",list);
        responseData.setData(list);
        return new ResponseEntity<>(responseData,HttpStatus.OK);

    }

    @PostMapping("/createpet")
    public ResponseEntity<?> createPet(@RequestHeader("Authorization") String jwt, @ModelAttribute CreatePetDTO createPetDTO) throws UserException, PetException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        PetDTO petDTO= petService.insertPet(createPetDTO,userDTO);
        responseData.setData(petDTO);
       return new ResponseEntity<>(responseData,HttpStatus.CREATED);
  }
    @GetMapping("/getAllPet")
    public ResponseEntity<?> getAllPet(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<PetDTO> list = petService.getAllPet(userDTO);
        request.setAttribute("listPet",list);
        responseData.setData(list);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }
    //sửa
    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<?> deletePet(@RequestHeader("Authorization") String jwt,@PathVariable(value = "id") Long id) throws PetException, UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        List<Exchange> exchanges = exchangeService.getAllExchange(userDTO);
        boolean isExist = false;
        for (Exchange exchange : exchanges) {
            if (exchange.getPet().getId().equals(id) && exchange.getStatus().equals(ExStatus.PENDING)) {
                isExist = true;
                break;
            }
        }
        if (isExist){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Pet existed in exchange.");
        }else{
            PetDTO petDTO = petService.deletePet(id,userDTO);
            if(petDTO==null){
                throw new PetException("Not found pet");
            }
            responseData.setData(petDTO);
            return new ResponseEntity<>(responseData,HttpStatus.OK);
        }
    }
    @GetMapping("/getOnePet/{id}")
    public ResponseEntity<?> getOnePet(@RequestHeader("Authorization") String jwt,@PathVariable(value = "id") Long id) throws UserException, PetException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        PetDTO petDTO = petService.getOnePet(id,userDTO);
        if(petDTO ==null){
           throw new PetException("Not Found");
        }
        responseData.setData(petDTO);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }
    @PutMapping("/updatePet/{id}")
    //@PostMapping ("/updatePet")
    public  ResponseEntity<?> updatePet(@PathVariable(value = "id") Long id, @ModelAttribute PetUpdateDTO petUpdateDTO,@RequestHeader("Authorization") String jwt) throws UserException, PetException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        PetDTO petDTO1 = petService.updatePet(id,petUpdateDTO,userDTO);
        if(petDTO1==null){
            throw new PetException("Not Found");
        }
        responseData.setData(petDTO1);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }


}
