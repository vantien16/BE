package com.petlover.petsocial.service;

import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.payload.request.ReactionDTO;
import com.petlover.petsocial.payload.request.UserDTO;

import java.util.List;

public interface ReactionService {
    ReactionDTO reactionPost(Long idPost, UserDTO userDTO) throws UserException, PostException;
    List<ReactionDTO> getAllReaction(Long idPost) throws PostException;
    ReactionDTO reactComment(Long idComment, UserDTO userDTO) throws UserException;
}
