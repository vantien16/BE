package com.petlover.petsocial.controller;


import com.petlover.petsocial.config.JwtProvider;
import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.exception.UserNotFoundException;
import com.petlover.petsocial.model.entity.AuthenticationProvider;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.payload.response.AuthResponse;
import com.petlover.petsocial.payload.response.ResponseData;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.PostService;
import com.petlover.petsocial.service.UserService;
import com.petlover.petsocial.serviceImp.CustomerUserDetailsServiceImp;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import jakarta.validation.Valid;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@RestController

public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    HttpSession session;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomerUserDetailsServiceImp customerUserDetailsServiceImp;
    @Autowired
    private PostService postService;


   @ModelAttribute
   public void commonUser(Principal p, Model m,@AuthenticationPrincipal OAuth2User usero2) {
       if (p != null) {
           String email = p.getName();
           User user = userRepo.findByEmail(email);
           m.addAttribute("user", user);
       }
       if(usero2 != null) {
           String email = usero2.getAttribute("email");
           User user = userRepo.findByEmail(email);
           m.addAttribute("user", user);
       }
   }


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

  @PostMapping("/createUser")
  public ResponseEntity<?> createuser(@RequestBody SingupDTO userDTO, HttpServletRequest request) throws UserException {
    String url = request.getRequestURL().toString();
    http://localhost:8080/createUser
    url = url.replace(request.getServletPath(), "");
    System.out.println(userDTO);
    boolean f = userService.checkEmail(userDTO.getEmail());
    ResponseData responseData = new ResponseData();
    if (f) {
//            throw new UserException("Email is already used with another account");
      responseData.setIsSuccess(false);
    } else {

      SingupDTO userDtls = userService.createUser(userDTO,url);
      Authentication authentication = new UsernamePasswordAuthenticationToken(userDTO.getEmail(),userDTO.getPassword());
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String token = jwtProvider.generateToken(authentication);
      AuthResponse res = new AuthResponse(token ,true,null);

      responseData.setData(res);

    }

    return new ResponseEntity<>(responseData.getIsSuccess(), HttpStatus.CREATED);
  }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninDTO signinDTO) throws UserException{
        ResponseData responseData = new ResponseData();
        // SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // String encrypted = Encoders.BASE64.encode(secretKey.getEncoded());
        //System.out.println(encrypted);

        System.out.println(signinDTO);
//    String token = jwtUtilHelper.generateToken(signinDTO.getUsername());
//    System.out.println(token);

//    responseData.setDescription(res.toString());
        String userLogin =  userService.checkLogin(signinDTO);
        if(userLogin.equals("Incorrect username or password")){
            responseData.setData("Incorrect");
        }else if(userLogin.equals("Your account has not been activated!")){
            responseData.setData("Activated");
        }else if(userLogin.equals("Account block")){
            responseData.setData("Account block");
        }else {
//      responseData.setToken(token);
//      responseData.setData(res);
            Authentication authentication = authenticate(signinDTO.getEmail(), signinDTO.getPassword());
            String token = jwtProvider.generateToken(authentication);
            AuthResponse res = new AuthResponse(token, true, null);
            System.out.println(token);
            responseData.setData(token);

        }
//        if(userService.checkLogin(signinDTO)){
//            String token = jwtUtilHelper.generateToken(signinDTO.getUsername());
//            responseData.setData(token);
//            User user = userService.getUserByEmail(signinDTO.getUsername());
//            session.setAttribute("user",user);
//
//        }else{
//            responseData.setData("");
//            responseData.setIsSuccess(false);
//        }

        return new ResponseEntity<>(responseData.getData(), HttpStatus.OK);
    }
    private Authentication authenticate( String username, String password) {
        UserDetails userDetails = customerUserDetailsServiceImp.loadUserByUsername(username);
        if(userDetails==null) {
            throw new BadCredentialsException("Invalid username..");

        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid username or password..");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code) {
        boolean f = userService.verifyAccount(code);

        if (f) {
            return "You can login. Sucessfully your account is verified";
        } else {
           return  "May be your vefication code is incorrect or already veified ";
        }


    }

    @GetMapping("/getAllPost")
    public ResponseEntity<?> getAllPost() throws UserException {
        ResponseData responseData = new ResponseData();

        List<PostDTO> list = postService.getAllPostHome();
        responseData.setData(list);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
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
    @GetMapping("/searchPost")
    public ResponseEntity<?> searchPost(@RequestParam("content") String content) throws UserException, PostException {
        ResponseData responseData = new ResponseData();

        List<PostDTO> list = postService.sreachPostHome(content);
        responseData.setData(list);
        return new ResponseEntity<>(responseData,HttpStatus.OK);

    }
    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO,HttpServletRequest request) {
        String token = RandomString.make(4);


        try {
            userService.updateResetPasswordToken(token,forgotPasswordDTO.getEmail());
            System.out.println("Email: " + forgotPasswordDTO.getEmail());
            System.out.println("Token: " + token);
            String url = request.getRequestURL().toString();
            url = url.replace(request.getServletPath(), "")  + "/reset_password?token=" + token;
            System.out.println(url);
//            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(forgotPasswordDTO.getEmail(), url);

//
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>("Error not found",HttpStatus.NOT_FOUND);
        } catch (UnsupportedEncodingException | MessagingException e) {
            return new ResponseEntity<>("Error",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(token,HttpStatus.OK);
    }

    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("phucvinh710@gmail.com", "Pet Social Media Support Reset Password");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token) {
        User user = userService.getByResetPasswordToken(token);
        if (user == null) {

            return "Invalid Code";
        }

        return "Code to reset password: " +token;
    }
    @PostMapping("/reset_password")
    public String processResetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        String gettoken = resetPasswordDTO.getToken();
        String pass =resetPasswordDTO.getPassword();

        User user = userService.getByResetPasswordToken(gettoken);
        if (user == null) {

            return "Invalid Code";
        } else {
            userService.updatePassword(user, pass);

        }

        return "Reset password correct !";
    }


}
