package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.config.JwtProvider;
import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.exception.UserNotFoundException;
import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.ReactionRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CloudinaryService;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.UserService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    HttpSession session;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReactionRepository reactionRepository;

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
    @Override
    public SingupDTO createUser(SingupDTO signupDTO, String url) {
        User user = new User();
        user.setEmail(signupDTO.getEmail());
        user.setName(signupDTO.getName());
        user.setPhone(signupDTO.getPhone());
        //String password = bCryptPasswordEncoder.encode(signupDTO.getPassword());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setRole("ROLE_USER");
        user.setEnable(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        User newuser = userRepo.save(user);

        if (newuser != null) {
            sendEmail(newuser, url);
        }
        SingupDTO newSignupDTO = new SingupDTO(newuser.getEmail(),newuser.getName(),newuser.getPassword(),newuser.getPhone());
        return newSignupDTO;
    }
    @Override
    public String checkLogin(SigninDTO signinDTO) {
        User user = userRepo.findByEmail(signinDTO.getEmail());
        if (user == null) {
            return "Incorrect username or password";
        } else if (!user.isEnable()) {
            return "Your account has not been activated!";
        } else if (passwordEncoder.matches(signinDTO.getPassword(), user.getPassword())) {
            return "Login success";
        } else {
            return "Incorrect username or password";

        }

    }



    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean checkEmail(String email) {

        return userRepo.existsByEmail(email);
    }

    @Override
    public void removeSessionMessage() {

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }



    @Override
    public void sendEmail(User user, String url) {
        String from = "phucvinh710@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verfication";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Vinh";

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, "Vinhhuynh");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getName());
            String siteUrl = url + "/verify?code=" + user.getVerificationCode();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyAccount(String verificationCode) {

        User user = userRepo.findByVerificationCode(verificationCode);

        if (user == null) {
            return false;
        } else {

            user.setEnable(true);
            user.setVerificationCode(null);

            userRepo.save(user);

            return true;
        }

    }
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User customer = userRepo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            userRepo.save(customer);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepo.save(user);
    }

    public User createUserAfterOAuthLoginSuccess(String email,String name, AuthenticationProvider provider) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setAuthProvider(provider);
        user.setEnable(true);
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }
    public User updateUserAfterOAuthLoginSuccess(User user ,String name) {
        user.setName(name);
        user.setAuthProvider(AuthenticationProvider.GOOGLE);

        return userRepo.save(user);
    }

    public UserDTO findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepo.findByEmail(email);
        if(user==null) {
           throw new UserException("user not found with email" + email);
        }
        List<PostDTO> postDTOList = new ArrayList<>();
        for(Post post: user.getPosts()){
            if(post.isStatus()==true) {
                if (post.isEnable() == true) {
                    PetToPostDTO petToPostDTO = new PetToPostDTO();
                    petToPostDTO.setId(post.getPet().getId());
                    petToPostDTO.setName(post.getPet().getName());
                    petToPostDTO.setImage(post.getPet().getImage());


                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(post.getUser().getId());
                    userPostDTO.setName(post.getUser().getName());
                    userPostDTO.setAvatar(post.getUser().getAvatar());


                    boolean fieldReaction = false;
                    List<Reaction> listReaction = reactionRepository.findAll();
                    for (Reaction reaction : listReaction) {
                        if (reaction.getUser().getId() == user.getId()) {
                            if(reaction.getPost().getId() == post.getId()) {
                                fieldReaction = true;
                            }
                        }
                    }
                    PostDTO postDTO = new PostDTO(post.getId(), post.getImage(), post.getContent(), post.getCreate_date(), post.getTotal_like(), post.getTotal_comment(), commentService.convertCommentListToDTO(post.getComments()), petToPostDTO, userPostDTO, fieldReaction);
                    postDTOList.add(postDTO);
                }
            }
        }

        List<PetDTO> petDTOList =new ArrayList<>();
        for(Pet pet: user.getPets()){
            if(pet.isStatus()==true) {
                PetDTO petDTO = new PetDTO(pet.getId(), pet.getImage(), pet.getName(), pet.getDescription());
                petDTOList.add(petDTO);
            }
        }

        return new UserDTO(user.getId(), user.getName(),user.getEmail(),user.getPhone(),user.getAvatar(),user.getRole(),petDTOList,postDTOList);
    }

    public UserDTO editprofile(Long id, UserUpdateDTO userDTO) throws UserException {
        User user = userRepo.getById(id);


        if(userDTO.getFile()==null)
        {
            if(userDTO.getName()!=null) {
                user.setName(userDTO.getName());
            }
            if(userDTO.getPhone()!=null) {
                user.setPhone(userDTO.getPhone());
            }
        }else {
            try {
                String image = cloudinaryService.uploadFile(userDTO.getFile());
                user.setAvatar(image);
            }catch (Exception e){

            }
            if(userDTO.getName()!=null) {
                user.setName(userDTO.getName());
            }
            if(userDTO.getPhone()!=null) {
                user.setPhone(userDTO.getPhone());
            }
        }
        userRepo.save(user);
        List<PostDTO> postDTOList = new ArrayList<>();
        for(Post post: user.getPosts()){
            PetToPostDTO petToPostDTO = new PetToPostDTO();
            petToPostDTO.setId(post.getPet().getId());
            petToPostDTO.setName(post.getPet().getName());
            petToPostDTO.setImage(post.getPet().getImage());


            UserPostDTO userPostDTO = new UserPostDTO();
            userPostDTO.setId(post.getUser().getId());
            userPostDTO.setName(post.getUser().getName());
            userPostDTO.setAvatar(post.getUser().getAvatar());
            boolean fieldReaction = false;
            List<Reaction> listReaction = reactionRepository.findAll();
            for (Reaction reaction : listReaction) {
                if (reaction.getUser().getId() == user.getId()) {
                    if(reaction.getPost().getId() == post.getId()) {
                        fieldReaction = true;
                    }
                }
            }

            PostDTO postDTO = new PostDTO(post.getId(),post.getImage(),post.getContent(),post.getCreate_date(),post.getTotal_like(), post.getTotal_comment(), commentService.convertCommentListToDTO(post.getComments()),petToPostDTO,userPostDTO,fieldReaction);
            postDTOList.add(postDTO);
        }

        List<PetDTO> petDTOList =new ArrayList<>();
        for(Pet pet: user.getPets()){
            PetDTO petDTO = new PetDTO(pet.getId(),pet.getImage(),pet.getName(),pet.getDescription());
            petDTOList.add(petDTO);
        }

        return new UserDTO(user.getId(), user.getName(),user.getEmail(),user.getPhone(),user.getAvatar(),user.getRole(),petDTOList,postDTOList);

    }

    public UserDTO findUserProfileById(Long idUser) throws UserException {

        User user = userRepo.getById(idUser);
        if(user==null) {
            throw new UserException("user not found" );
        }
        List<PostDTO> postDTOList = new ArrayList<>();
        for(Post post: user.getPosts()){
            if(post.isStatus()==true) {
                if(post.isEnable()==true) {
                    PetToPostDTO petToPostDTO = new PetToPostDTO();
                    petToPostDTO.setId(post.getPet().getId());
                    petToPostDTO.setName(post.getPet().getName());
                    petToPostDTO.setImage(post.getPet().getImage());


                    UserPostDTO userPostDTO = new UserPostDTO();
                    userPostDTO.setId(post.getUser().getId());
                    userPostDTO.setName(post.getUser().getName());
                    userPostDTO.setAvatar(post.getUser().getAvatar());

                    boolean fieldReaction = false;
                    List<Reaction> listReaction = reactionRepository.findAll();
                    for (Reaction reaction : listReaction) {
                        if (reaction.getUser().getId() == user.getId()) {
                            if(reaction.getPost().getId() == post.getId()) {
                                fieldReaction = true;
                            }
                        }
                    }
                    PostDTO postDTO = new PostDTO(post.getId(), post.getImage(), post.getContent(), post.getCreate_date(), post.getTotal_like(),post.getTotal_comment(), commentService.convertCommentListToDTO(post.getComments()), petToPostDTO, userPostDTO,fieldReaction);
                    postDTOList.add(postDTO);
                }
            }
        }

        List<PetDTO> petDTOList =new ArrayList<>();
        for(Pet pet: user.getPets()){
            if(pet.isStatus()==true) {
                PetDTO petDTO = new PetDTO(pet.getId(), pet.getImage(), pet.getName(), pet.getDescription());
                petDTOList.add(petDTO);
            }
        }

        return new UserDTO(user.getId(), user.getName(),user.getEmail(),user.getPhone(),user.getAvatar(),user.getRole(),petDTOList,postDTOList);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    public List<UserHomeDTO> getListUser() {
        List<User> userList = userRepo.listUserHome();
        List<UserHomeDTO> userHomeDTOList = new ArrayList<>();
        for(User user : userList) {
            UserHomeDTO userHomeDTO = new UserHomeDTO();
            userHomeDTO.setId(user.getId());
            userHomeDTO.setName(user.getName());
            userHomeDTO.setAvatar(user.getAvatar());
            userHomeDTOList.add(userHomeDTO);
        }

        return userHomeDTOList;
    }

    public List<UserHomeDTO> getSearchListUser(String name){
        List<User> userList = userRepo.listUserHome();
        List<UserHomeDTO> userHomeDTOList = new ArrayList<>();
        for(User user : userList) {
            if(user.getName().toLowerCase().contains(name.toLowerCase())) {
                UserHomeDTO userHomeDTO = new UserHomeDTO();
                userHomeDTO.setId(user.getId());
                userHomeDTO.setName(user.getName());
                userHomeDTO.setAvatar(user.getAvatar());
                userHomeDTOList.add(userHomeDTO);
            }
        }

        return userHomeDTOList;
    }



}
