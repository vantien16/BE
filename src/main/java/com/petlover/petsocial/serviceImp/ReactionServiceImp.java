package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.CommentRepository;
import com.petlover.petsocial.repository.PostRepository;
import com.petlover.petsocial.repository.ReactionRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.PostService;
import com.petlover.petsocial.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionServiceImp implements ReactionService {

   @Autowired
    ReactionRepository reactionRepository;
   @Autowired
    PostRepository postRepository;
   @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

   public ReactionDTO reactionPost(Long idPost, UserDTO userDTO) throws UserException, PostException {
       Reaction isReactionExist = reactionRepository.isReactionExist(userDTO.getId(),idPost);
       if(isReactionExist!=null) {
           reactionRepository.deleteById(isReactionExist.getId());
           Post post = postRepository.getById(idPost);
           int countReaction = getAllReaction(idPost).size();
           post.setTotal_like(countReaction);
           postRepository.save(post);
           PostDTO postDTO = postService.findById(idPost);
           return new ReactionDTO(isReactionExist.getId(),userDTO.getId(),postDTO.getId());
       }
       Post post = postRepository.getById(idPost);
       User user = userRepository.getById(userDTO.getId());
       Reaction reaction = new Reaction();
       reaction.setActive(true);
       reaction.setUser(user);
       reaction.setPost(post);
       Reaction saveReaction = reactionRepository.save(reaction);

       post.getReactions().add(saveReaction);
       int countReaction = getAllReaction(idPost).size();
       post.setTotal_like(countReaction);
       postRepository.save(post);
       PostDTO postDTO = postService.findById(idPost);
         return new ReactionDTO(saveReaction.getId(),userDTO.getId(),postDTO.getId());
   }

   public List<ReactionDTO> getAllReaction(Long idPost) throws PostException {
       PostDTO postDTO = postService.findById(idPost);
       List<Reaction> reactionList = reactionRepository.findByPostId(idPost);
        List<ReactionDTO> reactionDTOList = new ArrayList<>();
        for(Reaction reaction : reactionList){
            ReactionDTO reactionDTO = new ReactionDTO();
            reactionDTO.setId(reaction.getId());

            List<PostDTO> postDTOList = new ArrayList<>();
            for(Post post: reaction.getUser().getPosts()){
                PetToPostDTO petToPostDTO = new PetToPostDTO();
                if(post.getPet()!=null) {
                    petToPostDTO.setId(post.getPet().getId());
                    petToPostDTO.setName(post.getPet().getName());
                    petToPostDTO.setImage(post.getPet().getImage());
                }else{
                    petToPostDTO = null;
                }

                UserPostDTO userPostDTO = new UserPostDTO();
                userPostDTO.setId(post.getUser().getId());
                userPostDTO.setName(post.getUser().getName());
                userPostDTO.setAvatar(post.getUser().getAvatar());

                PostDTO postDTO2 = new PostDTO(post.getId(),post.getImage(),post.getContent(),post.getCreate_date(),post.getTotal_like(),post.getTotal_comment(),commentService.convertCommentListToDTO(post.getComments()),petToPostDTO,userPostDTO,true);
                postDTOList.add(postDTO2);
            }

            List<PetDTO> petDTOList =new ArrayList<>();
            for(Pet pet: reaction.getUser().getPets()){
                PetDTO petDTO = new PetDTO(pet.getId(),pet.getImage(),pet.getName(),pet.getDescription());
                petDTOList.add(petDTO);
            }

            UserDTO userDTO = new UserDTO(reaction.getUser().getId(),reaction.getUser().getName(),reaction.getUser().getEmail(),reaction.getUser().getPhone(),reaction.getUser().getAvatar(),reaction.getUser().getRole(),petDTOList,postDTOList);
            reactionDTO.setUserId(userDTO.getId());
            PostDTO postDTO1 = postService.findById(idPost);
            reactionDTO.setPostId(postDTO1.getId());
            reactionDTOList.add(reactionDTO);
        }
        return reactionDTOList;
   }

    public ReactionDTO reactComment(Long idComment, UserDTO userDTO) throws UserException {
        Comment comment = commentRepository.getById(idComment);
        User user = userRepository.getById(userDTO.getId());
        Reaction reaction = new Reaction();
        reaction.setActive(true);
        reaction.setUser(user);
        Reaction savedReaction = reactionRepository.save(reaction);

        return convertToDTO(savedReaction);
    }

    private ReactionDTO convertToDTO(Reaction reaction) {
        ReactionDTO dto = new ReactionDTO();
        dto.setId(reaction.getId());
        dto.setActive(reaction.isActive());
        dto.setUserId(reaction.getUser().getId());
        if (reaction.getPost() != null) {
            dto.setPostId(reaction.getPost().getId());
        }
        return dto;
    }
}
