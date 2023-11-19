package com.petlover.petsocial.controller;


import com.petlover.petsocial.config.JwtProvider;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.payload.response.AuthResponse;
import com.petlover.petsocial.payload.response.ResponseData;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.AdminService;
import com.petlover.petsocial.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtProvider jwtProvider;


    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllAccount(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            List<UserForAdminManager> list = adminService.getListUserForAdmin();
            responseData.setData(list);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{idUser}/block")
    public ResponseEntity<?> getBlockUser(@PathVariable Long idUser,@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            UserForAdminDTO userForAdminDTO = adminService.getBlockUser(idUser);
            responseData.setData(userForAdminDTO);
            return new ResponseEntity<>(responseData, HttpStatus.OK);

        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{idUser}/offblock")
    public ResponseEntity<?> getOffBlockUser(@PathVariable Long idUser,@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            UserForAdminDTO userForAdminDTO = adminService.getOffBlockUser(idUser);
            responseData.setData(userForAdminDTO);
            return new ResponseEntity<>(responseData, HttpStatus.OK);

        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/totaluser")
    public ResponseEntity<?> getTotalUser(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            int total = adminService.getTotalUser();
            responseData.setData(total);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSearchUser(@RequestParam("name") String name,@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            List<UserForAdminDTO> list = adminService.searchUser(name);
            responseData.setData(list);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPost")
    public ResponseEntity<?> getAllPost(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            List<PostForAdminDTO> list = adminService.listAllPost();
            responseData.setData(list);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPet")
    public ResponseEntity<?> getAllPet(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());
        if(user.getRole().equals("ROLE_ADMIN")){
            List<PetForAdminDTO> list = adminService.listAllPet();
            responseData.setData(list);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }else{
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/statistics")
    public ResponseEntity<?> getTotalStatistics(@RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO.getId());

        if(user.getRole().equals("ROLE_ADMIN")){
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String formattedDate = dateFormat.format(date);
            // Sử dụng các service để tính toán thông tin thống kê
            int totalUser = adminService.getTotalUser();
            int totalPostDelete = adminService.getTotalPostDete();
            int totalPet = adminService.getTotalPetDisplay();
            int totalPostDisplay = adminService.getTotalPostDisplay();
            int totalExchange = adminService.getTotalExchangeDisplay();
            double balance = adminService.getTotalBalance();

            // Tạo một đối tượng JSON để chứa thông tin thống kê
            Map<String, Integer> monthlyStatistics = new HashMap<>();
            for (int month = 1; month <= 12; month++) {
                int totalPostInMonth = adminService.getTotalPostDisplayInMonth(month);
                monthlyStatistics.put("totalPostInMonth" + (month < 10 ? "0" : "") + month, totalPostInMonth);
            }
            Map<String, Integer> monthlyExchangeStatistics = new HashMap<>();
            for (int month = 1; month <= 12; month++) {
                int totalExchangeInMonth = adminService.getTotalExchangeInMonth(month);
                monthlyExchangeStatistics.put("totalExchangeInMonth" + (month < 10 ? "0" : "") + month, totalExchangeInMonth);
            }


            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalUser", totalUser);
            statistics.put("totalPostDelete", totalPostDelete);
            statistics.put("totalPet", totalPet);
            statistics.put("totalPostDisplay", totalPostDisplay);
            statistics.put("monthlyStatistics", monthlyStatistics);
            statistics.put("totalExchange", totalExchange);
            statistics.put("monthlyExchangeStatistics", monthlyExchangeStatistics);
            statistics.put("totalBalance", balance);

            responseData.setData(statistics);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } else {
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createStaff")
    public ResponseEntity<?> createStaff(@RequestBody SingupDTO userDTO, @RequestHeader("Authorization") String jwt) throws UserException {
        ResponseData responseData = new ResponseData();
        UserDTO userDTO1 = userService.findUserProfileByJwt(jwt);
        User user = userRepo.getById(userDTO1.getId());
        if(user.getRole().equals("ROLE_ADMIN")) {
            System.out.println(userDTO);
            boolean f = userService.checkEmail(userDTO.getEmail());
            if (f) {
//            throw new UserException("Email is already used with another account");
                responseData.setIsSuccess(false);
            } else {

                SingupDTO userDtls = adminService.createStaff(userDTO);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtProvider.generateToken(authentication);
                AuthResponse res = new AuthResponse(token, true, null);

                responseData.setData(res);

            }

            return new ResponseEntity<>(responseData.getIsSuccess(), HttpStatus.CREATED);
        } else {
            responseData.setData("Only Admin");
            responseData.setStatus(403);
            responseData.setIsSuccess(false);
            return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
        }

    }
}
