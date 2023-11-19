package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.exception.PetException;
import com.petlover.petsocial.exception.PostException;
import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.PetRepository;
import com.petlover.petsocial.repository.PostRepository;
import com.petlover.petsocial.repository.ReactionRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CloudinaryService;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.PostService;
import com.petlover.petsocial.service.ReactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DateFormat;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Service
public class PostServiceImp implements PostService {
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    PetRepository petRepository;
    @Autowired
    HttpSession session;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReactionRepository reactionRepository;

    public PostDTO insertPost(CreatPostDTO creatPostDTO, UserDTO userDTO) throws PetException {
        Post newPost = new Post();


            if(creatPostDTO.getFile()!=null) {
                if(creatPostDTO.getIdPet() != null) {
                    Pet pet = petRepository.getById(creatPostDTO.getIdPet());
                    if(pet == null) {
                        throw new PetException("Not found Pet");
                    }
                    System.out.println(pet);

                    try {
                        String image = cloudinaryService.uploadFile(creatPostDTO.getFile());
                        newPost.setImage(image);
                    }catch (Exception e){}


                    newPost.setContent(creatPostDTO.getContent());
                    User user = userRepo.getById(userDTO.getId());
                    newPost.setUser(user);
                    newPost.setPet(pet);
                    newPost.setStatus(true);
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String formattedDate = dateFormat.format(date);
                    newPost.setCreate_date(formattedDate);
                    if(!user.getRole().equals("ROLE_USER")) {
                        newPost.setEnable(true);
                    }else{
                        newPost.setEnable(false);
                    }
                    newPost.setTotal_like(0);
                    postRepository.save(newPost);
                    PetToPostDTO petToPostDTO = new PetToPostDTO();
                    petToPostDTO.setId(newPost.getPet().getId());
                    petToPostDTO.setName(newPost.getPet().getName());
                    petToPostDTO.setImage(newPost.getPet().getImage());


                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(newPost.getUser().getId());
                    userPostDTO.setName(newPost.getUser().getName());
                    userPostDTO.setAvatar(newPost.getUser().getAvatar());

                    return new PostDTO(newPost.getId(),newPost.getImage(),newPost.getContent(),newPost.getCreate_date(),newPost.getTotal_like(), newPost.getTotal_comment(), commentService.convertCommentListToDTO(newPost.getComments()),petToPostDTO,userPostDTO,false);
                }else{
                    try {
                        String image = cloudinaryService.uploadFile(creatPostDTO.getFile());
                        newPost.setImage(image);
                    }catch (Exception e){}


                    newPost.setContent(creatPostDTO.getContent());
                    User user = userRepo.getById(userDTO.getId());
                    newPost.setUser(user);
                    newPost.setStatus(true);
                    newPost.setPet(null);
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String formattedDate = dateFormat.format(date);
                    newPost.setCreate_date(formattedDate);
                    if(!user.getRole().equals("ROLE_USER")) {
                        newPost.setEnable(true);
                    }else{
                        newPost.setEnable(false);
                    }
                    newPost.setTotal_like(0);
                    postRepository.save(newPost);


                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(newPost.getUser().getId());
                    userPostDTO.setName(newPost.getUser().getName());
                    userPostDTO.setAvatar(newPost.getUser().getAvatar());

                    return new PostDTO(newPost.getId(),newPost.getImage(),newPost.getContent(),newPost.getCreate_date(),newPost.getTotal_like(), newPost.getTotal_comment(), commentService.convertCommentListToDTO(newPost.getComments()),null,userPostDTO,false);
                }


            }else {
                if(creatPostDTO.getIdPet()!=null) {
                    Pet pet = petRepository.getById(creatPostDTO.getIdPet());
                    if (pet == null) {
                        throw new PetException("Not found Pet");
                    }
                    System.out.println(pet);
                    newPost.setImage(null);
                    if (creatPostDTO.getContent().equals("")) {
                        return null;
                    }
                    if (creatPostDTO.getContent() == null) {
                        return null;
                    }
                    newPost.setContent(creatPostDTO.getContent());
                    User user = userRepo.getById(userDTO.getId());
                    newPost.setUser(user);
                    newPost.setPet(pet);
                    newPost.setStatus(true);
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String formattedDate = dateFormat.format(date);
                    newPost.setCreate_date(formattedDate);
                    if (!user.getRole().equals("ROLE_USER")) {
                        newPost.setEnable(true);
                    } else {
                        newPost.setEnable(false);
                    }
                    newPost.setTotal_like(0);
                    postRepository.save(newPost);
                    PetToPostDTO petToPostDTO = new PetToPostDTO();
                    petToPostDTO.setId(newPost.getPet().getId());
                    petToPostDTO.setName(newPost.getPet().getName());
                    petToPostDTO.setImage(newPost.getPet().getImage());


                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(newPost.getUser().getId());
                    userPostDTO.setName(newPost.getUser().getName());
                    userPostDTO.setAvatar(newPost.getUser().getAvatar());

                    return new PostDTO(newPost.getId(),newPost.getImage(),newPost.getContent(),newPost.getCreate_date(),newPost.getTotal_like(), newPost.getTotal_comment(), commentService.convertCommentListToDTO(newPost.getComments()),petToPostDTO,userPostDTO,false);
                }else{
                    newPost.setImage(null);
                    if (creatPostDTO.getContent().equals("")) {
                        return null;
                    }
                    if (creatPostDTO.getContent() == null) {
                        return null;
                    }
                    newPost.setContent(creatPostDTO.getContent());
                    User user = userRepo.getById(userDTO.getId());
                    newPost.setUser(user);
                    newPost.setPet(null);
                    newPost.setStatus(true);
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    String formattedDate = dateFormat.format(date);
                    newPost.setCreate_date(formattedDate);
                    if (!user.getRole().equals("ROLE_USER")) {
                        newPost.setEnable(true);
                    } else {
                        newPost.setEnable(false);
                    }
                    newPost.setTotal_like(0);
                    postRepository.save(newPost);


                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(newPost.getUser().getId());
                    userPostDTO.setName(newPost.getUser().getName());
                    userPostDTO.setAvatar(newPost.getUser().getAvatar());

                    return new PostDTO(newPost.getId(),newPost.getImage(),newPost.getContent(),newPost.getCreate_date(),newPost.getTotal_like(), newPost.getTotal_comment(), commentService.convertCommentListToDTO(newPost.getComments()),null,userPostDTO,false);

                }

            }


    }

    public List<PostDTO> getAllPost(UserDTO userDTO)
    {
        List<Post> postList = postRepository.getAll();
        List<PostDTO> listpostDTO = new ArrayList<>();
        for(Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setContent(post.getContent());
            postDTO.setImage(post.getImage());
            postDTO.setCreate_date(post.getCreate_date());
            postDTO.setTotal_like(post.getTotal_like());
            postDTO.setTotal_comment(commentService.countCommentsByPostId(post.getId()));
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

            List<Reaction> listReaction = reactionRepository.findAll();
            for(Reaction reaction : listReaction)
            {
                if(reaction.getUser().getId() == userDTO.getId()) {
                    if(reaction.getPost().getId() == post.getId()) {
                        postDTO.setFieldReaction(true);
                    }else{
                        postDTO.setFieldReaction(false);
                    }

                }else{
                    postDTO.setFieldReaction(false);
                }
            }

            listpostDTO.add(postDTO);
        }
        return listpostDTO;
    }
    public List<PostDTO> getAllPostHome()
    {
        List<Post> postList = postRepository.getAll();
        List<PostDTO> listpostDTO = new ArrayList<>();
        for(Post post : postList) {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setContent(post.getContent());
            postDTO.setImage(post.getImage());
            postDTO.setCreate_date(post.getCreate_date());
            postDTO.setTotal_like(post.getTotal_like());
            postDTO.setTotal_comment(commentService.countCommentsByPostId(post.getId()));
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

    public List<PostDTO> getAllYourPost(Long idUser)
    {
        List<Post> postList = postRepository.getAllYourPost(idUser);
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
            List<Reaction> listReaction = reactionRepository.findAll();
            for(Reaction reaction : listReaction)
            {
                if(reaction.getUser().getId() == idUser) {
                    if(reaction.getPost().getId() == post.getId()) {
                        postDTO.setFieldReaction(true);
                    }else{
                        postDTO.setFieldReaction(false);
                    }
                }else{
                    postDTO.setFieldReaction(false);
                }
            }
            listpostDTO.add(postDTO);
        }
        return listpostDTO;
    }
    public List<PostDTO> sreachPost(String content,UserDTO userDTO)
    {
        List<Post> postListSearch = postRepository.searchPost(content);
        List<PostDTO> listpostDTO = new ArrayList<>();
        for(Post post : postListSearch) {
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
            List<Reaction> listReaction = reactionRepository.findAll();
            for(Reaction reaction : listReaction)
            {
                if(reaction.getUser().getId() == userDTO.getId()) {
                    if(reaction.getPost().getId() == post.getId()) {
                        postDTO.setFieldReaction(true);
                    }else{
                        postDTO.setFieldReaction(false);
                    }
                }else{
                    postDTO.setFieldReaction(false);
                }
            }
            listpostDTO.add(postDTO);
        }
        return listpostDTO;
    }


    public List<PostDTO> sreachPostHome(String content)
    {
        List<Post> postListSearch = postRepository.searchPost(content);
        List<PostDTO> listpostDTO = new ArrayList<>();
        for(Post post : postListSearch) {
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



    public PostDTO findById(Long idPost) throws PostException{
        Post getPost = postRepository.getById(idPost);
        if(getPost == null){
            throw new PostException("Not found");
        }
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
        boolean fieldReaction =false;

        return new PostDTO(getPost.getId(),getPost.getImage(),getPost.getContent(),getPost.getCreate_date(),getPost.getTotal_like(), getPost.getTotal_comment(), commentService.convertCommentListToDTO(getPost.getComments()),petToPostDTO,userPostDTO,fieldReaction);
    }
    public PostDTO deletePost(Long id, UserDTO userDTO)  {

        Post getPost = postRepository.getById(id);
        if(getPost == null) {
            return null;
        }
        if(getPost.getUser().getId() == userDTO.getId()) {
           getPost.setStatus(false);
        }
        else {
            return null;
        }
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

        boolean fieldReaction =false;
        List<Reaction> listReaction = reactionRepository.findAll();
        for(Reaction reaction : listReaction)
        {
            if(reaction.getUser().getId() == userDTO.getId()) {
                if(reaction.getPost().getId() == getPost.getId()) {
                    fieldReaction = true;
                }
            }
        }

        return new PostDTO(getPost.getId(),getPost.getImage(),getPost.getContent(),getPost.getCreate_date(),getPost.getTotal_like(), getPost.getTotal_comment(), commentService.convertCommentListToDTO(getPost.getComments()),petToPostDTO,userPostDTO,fieldReaction);
    }

    public PostDTO updatePost(Long id, PostUpdateDTO postUpdateDTO, UserDTO userDTO) {
        Post getPost = postRepository.getById(id);
        if (getPost == null) {
            return null; // Post not found
        }

        if (getPost.getUser().getId() == userDTO.getId()) {
            if (postUpdateDTO.getContent() != null && !postUpdateDTO.getContent().isEmpty()) {
                getPost.setContent(postUpdateDTO.getContent());
                getPost.setEnable(false);
                postRepository.save(getPost);
            }

            // Step 4: Construct and return the updated PostDTO
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

            boolean fieldReaction = false;
            List<Reaction> listReaction = reactionRepository.findAll();
            for (Reaction reaction : listReaction) {
                if (reaction.getUser().getId() == userDTO.getId()) {
                    if (reaction.getPost().getId() == getPost.getId()) {
                        fieldReaction = true;
                    }
                }
            }

            return new PostDTO(
                    getPost.getId(),
                    getPost.getImage(),
                    getPost.getContent(),
                    getPost.getCreate_date(),
                    getPost.getTotal_like(),
                    getPost.getTotal_comment(),
                    commentService.convertCommentListToDTO(getPost.getComments()),
                    petToPostDTO,
                    userPostDTO,
                    fieldReaction
            );
        } else {
            return null; // User doesn't have permission to update the post
        }
    }

}
