package com.petlover.petsocial.controller;

import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.payload.request.PostDTO;
import com.petlover.petsocial.payload.request.UserDTO;
import com.petlover.petsocial.payload.response.ResponseData;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.PostService;
import com.petlover.petsocial.service.StaffService;
import com.petlover.petsocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    StaffService staffService;

    @GetMapping("/getAllPostDisable")
    public ResponseEntity<?> getAllPostDisable(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_STAFF")){
            List<PostDTO> list = staffService.getAllPostDisable();
            responseData.setData(list);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }else{
            responseData.setData("Only Staff");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{idPost}/enable")
    public ResponseEntity<?> getEnablePost(@PathVariable Long idPost,@RequestHeader("Authorization") String jwt) throws UserException, PostException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_STAFF")){
          PostDTO post = staffService.getEnablePost(idPost);
            responseData.setData(post);
            return new ResponseEntity<>(responseData, HttpStatus.OK);

        }else{
            responseData.setData("Only Staff");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

}
