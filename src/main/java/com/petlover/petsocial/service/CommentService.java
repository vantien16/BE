package com.petlover.petsocial.service;


import com.petlover.petsocial.model.entity.Comment;
import com.petlover.petsocial.model.entity.Pet;
import com.petlover.petsocial.model.entity.Post;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.payload.request.CommentDTO;
import com.petlover.petsocial.payload.request.PetDTO;
import com.petlover.petsocial.payload.request.PostDTO;
import com.petlover.petsocial.payload.request.UserDTO;

import java.util.List;

public interface CommentService {
    CommentDTO getCommentById(Long id);
    List<CommentDTO> getCommentsByPostId(Long postId);
    CommentDTO createComment(Long userId, Long postId, CommentDTO comment);
    CommentDTO updateComment(Long id, CommentDTO commentDetails);
    void deleteComment(Long commentId);
    List<CommentDTO> convertCommentListToDTO(List<Comment> commentList);
    CommentDTO convertCommentToDTO(Comment comment);
    UserDTO convertUserToDTO(User user);
    List<PetDTO> convertPetListToDTOs(List<Pet> petList);
    List<PostDTO> convertPostListToDTOs(List<Post> postList);
    int countCommentsByPostId(Long postId);
}
