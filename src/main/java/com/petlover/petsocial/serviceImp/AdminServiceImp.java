package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.ExchangeRepository;
import com.petlover.petsocial.repository.PetRepository;
import com.petlover.petsocial.repository.PostRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.AdminService;
import com.petlover.petsocial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AdminServiceImp implements AdminService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PetRepository petRepository;

    @Autowired
    ExchangeRepository exchangeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserForAdminManager> getListUserForAdmin()
    {
        List<User> listUser = userRepo.listUser();
        List<UserForAdminManager> getListUserForAdmin = new ArrayList<>();
        for(User user : listUser) {
            UserForAdminManager userForAdminDTO = new UserForAdminManager();
            userForAdminDTO.setId(user.getId());
            userForAdminDTO.setName(user.getName());
            userForAdminDTO.setAvatar(user.getAvatar());
            userForAdminDTO.setEnable(user.isEnable());
            userForAdminDTO.setEmail(user.getEmail());
            userForAdminDTO.setBalance(user.getBalance());
            userForAdminDTO.setPhone(user.getPhone());
            userForAdminDTO.setRole(user.getRole());
            if(user.getAuthProvider()!=null) {
                userForAdminDTO.setAuthProvider(user.getAuthProvider());
            }else{
                userForAdminDTO.setAuthProvider(AuthenticationProvider.NOT_BLOCK_USER);
            }
            int countpet=0;
            for(int i=0;i<user.getPets().size();i++) {
                if(user.getPets().get(i).isStatus()==true) {
                    countpet++;
                }
            }
            userForAdminDTO.setTotalPet(countpet);
            int countpost=0;
            for(int y=0;y<user.getPosts().size();y++) {
                if(user.getPosts().get(y).isStatus()==true) {
                    if(user.getPosts().get(y).isEnable()==true) {
                        countpost++;
                    }
                }
            }
            userForAdminDTO.setTotalPost(countpost);
            getListUserForAdmin.add(userForAdminDTO);
        }
        return getListUserForAdmin;
    }

    public UserForAdminDTO getBlockUser(Long idUser) throws UserException {
        User user = userRepo.getById(idUser);
        if(user ==null) {
            throw new UserException("Not found User");
        }
        if(user.getAuthProvider() == AuthenticationProvider.BLOCK_USER){
            throw new UserException("User are blocked");
        }
        user.setAuthProvider(AuthenticationProvider.BLOCK_USER);
        userRepo.save(user);
        UserForAdminDTO userForAdminDTO = new UserForAdminDTO();
        userForAdminDTO.setId(user.getId());
        userForAdminDTO.setName(user.getName());
        userForAdminDTO.setAvatar(user.getAvatar());
        userForAdminDTO.setEnable(user.isEnable());
        userForAdminDTO.setEmail(user.getEmail());
        userForAdminDTO.setPhone(user.getPhone());
        userForAdminDTO.setRole(user.getRole());
        return userForAdminDTO;
    }

    public UserForAdminDTO getOffBlockUser(Long idUser) throws UserException {
        User user = userRepo.getById(idUser);
        if(user ==null) {
            throw new UserException("Not found User");
        }
        if(user.getAuthProvider() ==null){
            throw new UserException("User not block");
        }
        user.setAuthProvider(null);
        userRepo.save(user);
        UserForAdminDTO userForAdminDTO = new UserForAdminDTO();
        userForAdminDTO.setId(user.getId());
        userForAdminDTO.setName(user.getName());
        userForAdminDTO.setAvatar(user.getAvatar());
        userForAdminDTO.setEnable(user.isEnable());
        userForAdminDTO.setEmail(user.getEmail());
        userForAdminDTO.setPhone(user.getPhone());
        userForAdminDTO.setRole(user.getRole());
        return userForAdminDTO;
    }

    public int getTotalUser() {
        List<User> listUser = userRepo.listUser();
        return listUser.size();
    }

    public int getTotalPostDete() {
        int size =0;
        try {
            List<Post> listPostDelete = postRepository.getAllPostDeleteForAdmin();
            size = listPostDelete.size();
        }catch(Exception ex){ }
        return size;
    }

    public int getTotalPostDisplay() {
        int size =0;
        try {
            List<Post> listPostDisplay = postRepository.getAllPostDisplayUserForAdmin();
            size = listPostDisplay.size();
        }catch(Exception ex){ }
        return size;
    }
    public int getTotalPetDisplay() {
        int size =0;
        try {
            List<Pet> listPetDisplay = petRepository.getAllPetDisplayForAdmin();
            size = listPetDisplay.size();
        }catch(Exception ex){ }
        return size;
    }

    public int getTotalPostDisplayInMonth(int month) {
        int count = 0;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormat.format(date);
        LocalDate localDateNow = LocalDate.parse(formattedDate,DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        List<Post> listPostDisplay = postRepository.getAllPostDisplayUserForAdmin();
        for (Post post : listPostDisplay) {
            String postDate = post.getCreate_date();
            LocalDate localDate = LocalDate.parse(postDate, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

            if (localDate.getMonthValue() == month) {
                if(localDate.getYear() == localDateNow.getYear())
                    count++;
            }
        }
        return count;
    }

    @Override
    public int getTotalExchangeDisplay() {
        int size =0;
        try {
            List<Exchange> listExchangeDisplay = exchangeRepository.getAllExchange();
            size = listExchangeDisplay.size();
        }catch(Exception ex){ }
        return size;
    }

    @Override
    public int getTotalExchangeInMonth(int month) {
        int count = 0;
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = dateFormat.format(date);
        LocalDate localDateNow = LocalDate.parse(formattedDate,DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        List<Exchange> listExchangeDisplay = exchangeRepository.getAllExchange();
        for (Exchange e : listExchangeDisplay) {
            Date exchangeDate = e.getExchange_date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String dateStr = sdf.format(exchangeDate);
            LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

            if (localDate.getMonthValue() == month) {
                if(localDate.getYear() == localDateNow.getYear())
                    count++;
            }
        }
        return count;
    }

    @Override
    public double getTotalBalance() {
        Double totalBalance = userRepo.getTotalBalance();
        return totalBalance != null ? totalBalance : 0.0;
    }

    public List<UserForAdminDTO> searchUser(String name) {
        List<User> listUser = userRepo.listUser();
        List<UserForAdminDTO> getListUserForAdmin = new ArrayList<>();
        for(User user : listUser) {
            if(user.getName().toLowerCase().contains(name.toLowerCase())){
                UserForAdminDTO userForAdminDTO = new UserForAdminDTO();
                userForAdminDTO.setId(user.getId());
                userForAdminDTO.setName(user.getName());
                userForAdminDTO.setAvatar(user.getAvatar());
                userForAdminDTO.setEnable(user.isEnable());
                userForAdminDTO.setEmail(user.getEmail());
                userForAdminDTO.setPhone(user.getPhone());
                userForAdminDTO.setRole(user.getRole());
                getListUserForAdmin.add(userForAdminDTO);
            }
        }
        return getListUserForAdmin;
    }


    public List<PostForAdminDTO> listAllPost() {
        List<Post> listPost = postRepository.getAllPostForAdmin();
        List<PostForAdminDTO> postForAdminDTOS = new ArrayList<>();
        for(Post post : listPost) {
            PostForAdminDTO postDTO = new PostForAdminDTO();
            postDTO.setId(post.getId());
            postDTO.setContent(post.getContent());
            postDTO.setImage(post.getImage());
            postDTO.setCreate_date(post.getCreate_date());
            postDTO.setTotal_like(post.getTotal_like());
            postDTO.setTotal_comment(post.getComments().size());
            postDTO.setUser_name(post.getUser().getName());
            postDTO.setStatus(post.isStatus());
            postDTO.setEnable(post.isEnable());
            postForAdminDTOS.add(postDTO);
        }
        return postForAdminDTOS;
    }

    public List<PetForAdminDTO> listAllPet() {
        List<Pet> listPet = petRepository.getAllPetForAdmin();
        List<PetForAdminDTO> petForAdminDTOS = new ArrayList<>();
        for(Pet pet : listPet) {
            PetForAdminDTO petForAdminDTO = new PetForAdminDTO();
            petForAdminDTO.setId(pet.getId());
            petForAdminDTO.setName(pet.getName());
            petForAdminDTO.setDescription(pet.getDescription());
            petForAdminDTO.setImage(pet.getImage());
            petForAdminDTO.setPetType_name(pet.getPet_type().getName());
            petForAdminDTO.setUser_name(pet.getUser().getName());
            petForAdminDTO.setStatus(pet.isStatus());
            petForAdminDTOS.add(petForAdminDTO);
        }
        return petForAdminDTOS;
    }


    public SingupDTO createStaff(SingupDTO signupDTO) {
        User user = new User();
        user.setEmail(signupDTO.getEmail());
        user.setName(signupDTO.getName());
        user.setPhone(signupDTO.getPhone());
        //String password = bCryptPasswordEncoder.encode(signupDTO.getPassword());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setAvatar("https://res.cloudinary.com/dyrprccxf/image/upload/v1699728147/zoaodn4jg4dalzoghhx3.jpg");
        user.setRole("ROLE_STAFF");
        user.setEnable(true);
        User newuser = userRepo.save(user);
        SingupDTO newSignupDTO = new SingupDTO(newuser.getEmail(),newuser.getName(),newuser.getPassword(),newuser.getPhone());
        return newSignupDTO;
    }


}
