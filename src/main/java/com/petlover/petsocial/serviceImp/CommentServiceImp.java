package com.petlover.petsocial.serviceImp;


import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.CommentRepository;
import com.petlover.petsocial.repository.PostRepository;
import com.petlover.petsocial.repository.ReactionRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.ReactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImp implements CommentService {


    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReactionRepository reactionRepository;
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        // Convert to DTO
        return comments.stream()
                .map(c -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setId(c.getId());
                    dto.setContent(c.getContent());
                    dto.setUserDTO(this.convertUserToDTO(c.getUser()));
                    dto.setPostId(c.getPost().getId());
                    dto.setCreatedTime(c.getCreatedTime());
                    return dto;
                })
                .sorted(Comparator.comparing(CommentDTO::getCreatedTime).reversed())
                .collect(Collectors.toList());
    }
    public CommentDTO getCommentById(Long id) {

        // Fetch comment entity
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // Map to DTO
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserDTO(this.convertUserToDTO(comment.getUser()));
        dto.setPostId(comment.getPost().getId());
        dto.setCreatedTime(comment.getCreatedTime());

        return dto;

    }

    public CommentDTO createComment(Long userId, Long postId, CommentDTO commentDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedTime(LocalDateTime.now());
        post.setTotal_comment(post.getTotal_comment()+1);
        postRepository.save(post);
        commentRepository.save(comment);

        return new CommentDTO(comment.getId(),comment.getContent(),this.convertUserToDTO(comment.getUser()),comment.getPost().getId(),comment.getCreatedTime());
    }


    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);

        return new CommentDTO(comment.getId(),comment.getContent(),this.convertUserToDTO(comment.getUser()),comment.getPost().getId(),comment.getCreatedTime());
    }


    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    public List<PostDTO> convertPostListToDTOs(List<Post> postList) {
        List<PostDTO> postDTOList = new ArrayList<>();
        for(Post post: postList){
            if(post.isStatus()) {
                if(post.isEnable()) {
                    PetToPostDTO petToPostDTO = new PetToPostDTO();
                    if(post.getPet()!=null) {
                        petToPostDTO.setId(post.getPet().getId());
                        petToPostDTO.setName(post.getPet().getName());
                        petToPostDTO.setImage(post.getPet().getImage());
                    }else{
                        petToPostDTO=null;
                    }

                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(post.getUser().getId());
                    userPostDTO.setName(post.getUser().getName());
                    userPostDTO.setAvatar(post.getUser().getAvatar());

                    PostDTO postDTO = new PostDTO();
                    postDTO.setId(post.getId());
                    postDTO.setImage(post.getImage());
                    postDTO.setContent(post.getContent());
                    postDTO.setCreate_date(post.getCreate_date());
                    postDTO.setTotal_like(post.getTotal_like());
//                    postDTO.setComments(this.convertCommentListToDTO(post.getComments()));
                    postDTO.setPetToPostDTO(petToPostDTO);
                    postDTO.setUserPostDTO(userPostDTO);
                    postDTO.setFieldReaction(false);
                    postDTOList.add(postDTO);
                }
            }
        }
        return postDTOList;
    }
    public List<PetDTO> convertPetListToDTOs(List<Pet> petList) {
        List<PetDTO> petDTOList = new ArrayList<>();
        for(Pet pet: petList){
            if(pet.isStatus()) {
                PetDTO petDTO = new PetDTO();
                petDTO.setId(pet.getId());
                petDTO.setName(pet.getName());
                petDTO.setImage(pet.getImage());
                petDTO.setDescription(pet.getDescription());
                petDTOList.add(petDTO);
            }
        }
        return petDTOList;
    }



    public UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setPostDTOList(this.convertPostListToDTOs(user.getPosts()));
        userDTO.setRole(user.getRole());
        userDTO.setPetDTOList(this.convertPetListToDTOs(user.getPets()));
        return userDTO;
    }

    public CommentDTO convertCommentToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUserDTO(this.convertUserToDTO(comment.getUser()));
        commentDTO.setPostId(comment.getPost().getId());
        commentDTO.setCreatedTime(comment.getCreatedTime());
        return commentDTO;
    }

    public List<CommentDTO> convertCommentListToDTO(List<Comment> commentList) {
        if (commentList == null) {
            return new ArrayList<>(); // return an empty list if commentList is null
        }
        return commentList.stream().map(this::convertCommentToDTO).collect(Collectors.toList());
    }
    public int countCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.size();
    }
}
