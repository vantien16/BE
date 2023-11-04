package com.petlover.petsocial.service;

import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.payload.request.PetForAdminDTO;
import com.petlover.petsocial.payload.request.PostForAdminDTO;
import com.petlover.petsocial.payload.request.UserForAdminDTO;
import com.petlover.petsocial.payload.request.UserForAdminManager;

import java.util.List;

public interface AdminService {
    List<UserForAdminManager> getListUserForAdmin();
    UserForAdminDTO getBlockUser(Long idUser) throws UserException;
    UserForAdminDTO getOffBlockUser(Long  idUser) throws UserException;
    int getTotalUser();
    List<UserForAdminDTO> searchUser(String name);
    List<PostForAdminDTO> listAllPost();
    List<PetForAdminDTO> listAllPet();

    int getTotalPostDete();
    int getTotalPostDisplay();
    int getTotalPetDisplay();

    int getTotalPostDisplayInMonth(int month);

    int getTotalExchangeDisplay();
    int getTotalExchangeInMonth(int month);
}
