package com.petlover.petsocial.service;


import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.exception.UserNotFoundException;
import com.petlover.petsocial.model.entity.AuthenticationProvider;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.payload.request.*;

import java.math.BigDecimal;
import java.util.List;


public interface UserService {

    SingupDTO createUser(SingupDTO signupDTO, String url);
    String checkLogin(SigninDTO signinDTO);
    User getUserByEmail(String email);
    boolean checkEmail(String email);

    void removeSessionMessage();
    void sendEmail(User user, String path);
    boolean verifyAccount(String verificationCode);
    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
    User getByResetPasswordToken(String token);
    void updatePassword(User user, String newPassword);
    User createUserAfterOAuthLoginSuccess(String email,String name, AuthenticationProvider provider);
    User updateUserAfterOAuthLoginSuccess(User user ,String name);

    UserDTO findUserProfileByJwt(String jwt) throws UserException;
    UserDTO editprofile(Long id, UserUpdateDTO userDTO) throws UserException;
    UserDTO findUserProfileById(Long idUser) throws UserException;

    User findById(Long id);
    List<User> getAllUsers();

    List<UserHomeDTO> getListUser();
    List<UserHomeDTO> getSearchListUser(String name);
    User updateBalance(String jwt, BigDecimal amount) throws UserException;
    void storeJwtToken(String paymentId, String jwt);
    String retrieveJwtToken(String paymentId);
    User substractBalanceToCreateExchange(String jwt);

    BigDecimal getBalance(String jwt) throws UserException;
}
