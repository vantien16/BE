package com.petlover.petsocial.controller;



import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.payload.request.UserDTO;
import com.petlover.petsocial.payload.request.UserHomeDTO;
import com.petlover.petsocial.payload.request.UserUpdateDTO;
import com.petlover.petsocial.payload.response.ResponseData;
import com.petlover.petsocial.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    HttpSession session;
    @Autowired
    private UserService userService;

    //@AuthenticationPrincipal OAuth2User usero2

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        responseData.setData(userDTO);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String jwt, @ModelAttribute UserUpdateDTO userDTO) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO1 = userService.findUserProfileByJwt(jwt);

        UserDTO userDTO2 = userService.editprofile(userDTO1.getId(),userDTO);
        responseData.setData(userDTO2);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable(value = "id") Long id) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileById(id);
        responseData.setData(userDTO);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(){
        ResponseData responseData = new ResponseData();
        List<UserHomeDTO> list = userService.getListUser();
        responseData.setData(list);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }
    @GetMapping("/searchUser")
    public ResponseEntity<?> searchUser(@RequestParam("name") String name) throws UserException, PostException {
        ResponseData responseData = new ResponseData();
        List<UserHomeDTO> list = userService.getSearchListUser(name);
        responseData.setData(list);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }
}
