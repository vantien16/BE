package com.petlover.petsocial.service;

import com.petlover.petsocial.exception.PetException;
import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.payload.request.CreatPostDTO;
import com.petlover.petsocial.payload.request.PostDTO;
import com.petlover.petsocial.payload.request.PostUpdateDTO;
import com.petlover.petsocial.payload.request.UserDTO;

import java.util.List;

public interface PostService {
    PostDTO insertPost(CreatPostDTO creatPostDTO, UserDTO userDTO) throws PetException;
    List<PostDTO> getAllPost(UserDTO userDTO);
    List<PostDTO> getAllYourPost(Long idUser);
    PostDTO deletePost(Long id, UserDTO userDTO) throws PostException;
    PostDTO updatePost(Long id, PostUpdateDTO postUpdateDTO,UserDTO userDTO);
    PostDTO findById(Long idPost) throws PostException;
    List<PostDTO> sreachPost(String content, UserDTO userDTO);
    List<PostDTO> sreachPostHome(String content);
    List<PostDTO> getAllPostHome();
}
