package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.model.entity.Post;
import com.petlover.petsocial.model.entity.Reaction;
import com.petlover.petsocial.payload.request.PetToPostDTO;
import com.petlover.petsocial.payload.request.PostDTO;
import com.petlover.petsocial.payload.request.UserPostDTO;
import com.petlover.petsocial.repository.PostRepository;
import com.petlover.petsocial.repository.ReactionRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.StaffService;
import com.petlover.petsocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffServiceImp implements StaffService {
    @Autowired
    UserRepository userRepo;
    @Autowired
     UserService userService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReactionRepository reactionRepository;
   @Override
    public List<PostDTO> getAllPostDisable() {
        List<Post> postList = postRepository.getAllPostDisable();
        List<PostDTO> listpostDTO = new ArrayList<>();
        for(Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setContent(post.getContent());
            postDTO.setImage(post.getImage());
            postDTO.setCreate_date(post.getCreate_date());
            postDTO.setTotal_like(post.getTotal_like());
            postDTO.setComments(commentService.convertCommentListToDTO(post.getComments()));

            PetToPostDTO petToPostDTO = new PetToPostDTO();
            if(post.getPet()!=null) {
                petToPostDTO.setId(post.getPet().getId());
                petToPostDTO.setName(post.getPet().getName());
                petToPostDTO.setImage(post.getPet().getImage());
                postDTO.setPetToPostDTO(petToPostDTO);
            }else{
                postDTO.setPetToPostDTO(null);
            }
            UserPostDTO userPostDTO = new UserPostDTO();
            userPostDTO.setId(post.getUser().getId());
            userPostDTO.setName(post.getUser().getName());
            userPostDTO.setAvatar(post.getUser().getAvatar());
            postDTO.setUserPostDTO(userPostDTO);
            postDTO.setFieldReaction(false);
            listpostDTO.add(postDTO);
        }
        return listpostDTO;
    }

    public PostDTO getEnablePost(Long idPost) throws PostException {
       Post getPost = postRepository.getById(idPost);
       if(getPost == null){
           throw new PostException("Not found Post");
       }
       if(getPost.isEnable() == true) {
           throw new PostException("Post is enable !");
       }
        getPost.setEnable(true);
       postRepository.save(getPost);
        PetToPostDTO petToPostDTO = new PetToPostDTO();
        if(getPost.getPet()!=null) {
            petToPostDTO.setId(getPost.getPet().getId());
            petToPostDTO.setName(getPost.getPet().getName());
            petToPostDTO.setImage(getPost.getPet().getImage());
        }else{
            petToPostDTO =null;
        }

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setId(getPost.getUser().getId());
        userPostDTO.setName(getPost.getUser().getName());
        userPostDTO.setAvatar(getPost.getUser().getAvatar());



        return new PostDTO(getPost.getId(),getPost.getImage(),getPost.getContent(),getPost.getCreate_date(),getPost.getTotal_like(), getPost.getTotal_comment(), commentService.convertCommentListToDTO(getPost.getComments()),petToPostDTO,userPostDTO,false);

    }

}
