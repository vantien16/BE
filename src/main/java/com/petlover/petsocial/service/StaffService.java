package com.petlover.petsocial.service;

import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.payload.request.PostDTO;

import java.util.List;

public interface StaffService {
    List<PostDTO> getAllPostDisable();
    PostDTO getEnablePost(Long idPost) throws PostException;

    PostDTO getDeletePost(Long idPost) throws PostException;
}
