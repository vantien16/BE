package com.petlover.petsocial.serviceImp;

import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.payload.request.*;
import com.petlover.petsocial.repository.ExchangeRepository;
import com.petlover.petsocial.repository.PetRepository;
import com.petlover.petsocial.repository.UserRepository;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.ExchangeService;
import com.petlover.petsocial.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeServiceImp implements ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private CommentService commentService;


    @Override
    public ExchangeDTO addExchange(UserDTO userDTO, Long petId, int paymentAmount) {
        Exchange exchange = new Exchange();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        Pet pet = petRepository.getById(petId);
        if (user != null && pet != null) {
            exchange.setUser(user);
            exchange.setPet(pet);
            exchange.setExchange_date(new Date());
            exchange.setPayment_amount(paymentAmount);
            exchange.setStatus(ExStatus.PENDING);
            exchangeRepository.save(exchange);
            return convertToDTO(exchange);
        } else {
            return null;
        }
    }

    @Override
    public ExchangeDTO deleteExchange(UserDTO userDTO,Long id) {
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (exchange != null && exchange.getUser().getId()==user.getId()) {
            exchange.setStatus(ExStatus.REMOVED);
            exchangeRepository.save(exchange);
            return convertToDTO(exchange);
        } else {
            return null;

        }
    }
    @Override
    public List<ExchangeDTO> getAllExchangeToShow() {
        List<ExchangeDTO> exchangeDTOList = new ArrayList<>();
        for (Exchange exchange : exchangeRepository.getAllExchange()) {
            if(exchange.getStatus()==ExStatus.PENDING){
            ExchangeDTO exchangeDTO = convertToDTO(exchange);
            exchangeDTOList.add(exchangeDTO);}
        }
        return exchangeDTOList;
    }
    @Override
    public ExchangeDTO updateExchange(UserDTO userDTO,Long id) {
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (exchange != null && exchange.getUser().getId()==user.getId()) {
            exchange.setStatus(ExStatus.COMPLETED);
            exchangeRepository.save(exchange);
            return convertToDTO(exchange);
        } else {
            return null;

        }
    }

    @Override
    public ExchangeDTO editCashExchange(UserDTO userDTO, Long id, int paymentAmount) {
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        User user = userRepository.findById(userDTO.getId()).orElse(null);

        if(paymentAmount>0 && exchange.getUser().getId()==user.getId()){
            exchange.setPayment_amount(paymentAmount);
            exchangeRepository.save(exchange);
            return convertToDTO(exchange);
        }else {
            return null;

        }
    }

    @Override
    public List<ExchangeDTO> getAllExchangeDTO(UserDTO userDTO) {
        List<ExchangeDTO> exchangeList = new ArrayList<>();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            for (Exchange exchange : user.getExchanges()){
                if(exchange.getStatus()!=ExStatus.REMOVED){
                    ExchangeDTO exchangeDTO = convertToDTO(exchange);
                    exchangeList.add(exchangeDTO);
                }
            }
            return exchangeList;
        } else {
            return null;
        }

    }

    @Override
    public List<ExchangeDTO> getAllRemovedExchangeDTO(UserDTO userDTO) {
        List<Exchange> exchangeList = new ArrayList<>();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            exchangeList = user.getExchanges();
            List<ExchangeDTO> exchangeList1 = new ArrayList<>();
            for (Exchange exchange : exchangeList) {
                if (exchange.getStatus().equals(ExStatus.REMOVED)) {
                    ExchangeDTO exchangeDTO = convertToDTO(exchange);
                    exchangeList1.add(exchangeDTO);
                }
            }
            return exchangeList1;
        } else {
            return null;
        }
    }

    @Override
    public List<ExchangeDTO> getAllNotRemovedExchangeDTO(UserDTO userDTO) {
        List<Exchange> exchangeList = new ArrayList<>();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            exchangeList = user.getExchanges();
            List<ExchangeDTO> exchangeList1 = new ArrayList<>();
            for (Exchange exchange : exchangeList) {
                if (!exchange.getStatus().equals(ExStatus.REMOVED)) {
                    ExchangeDTO exchangeDTO = convertToDTO(exchange);
                    exchangeList1.add(exchangeDTO);
                }
            }
            return exchangeList1;
        } else {
            return null;
        }
    }

    @Override
    public List<Exchange> getAllExchange(UserDTO userDTO) {
        List<Exchange> exchangeList = new ArrayList<>();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            exchangeList = user.getExchanges();
            return exchangeList;
        } else {
            return null;
        }

    }

    @Override
    public List<Exchange> getAllRemovedExchange(UserDTO userDTO) {
        List<Exchange> exchangeList = new ArrayList<>();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            exchangeList = user.getExchanges();
            List<Exchange> exchangeList1 = new ArrayList<>();
            for (Exchange exchange : exchangeList) {
                if (exchange.getStatus().equals(ExStatus.REMOVED)) {
                    exchangeList1.add(exchange);
                }
            }
            return exchangeList1;
        } else {
            return null;
        }
    }

    @Override
    public List<Exchange> getAllNotRemovedExchange(UserDTO userDTO) {
        List<Exchange> exchangeList = new ArrayList<>();
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            exchangeList = user.getExchanges();
            List<Exchange> exchangeList1 = new ArrayList<>();
            for (Exchange exchange : exchangeList) {
                if (!exchange.getStatus().equals(ExStatus.REMOVED)) {
                    exchangeList1.add(exchange);
                }
            }
            return exchangeList1;
        } else {
            return null;
        }
    }

    @Override
    public ExchangeDTO getOneExchangeDTO(UserDTO userDTO,Long id) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        if(user.getId()==exchange.getUser().getId() && !exchange.getStatus().equals(ExStatus.REMOVED)){
            return convertToDTO(exchange);
        }
        else {
            return null;
        }

    }

    @Override
    public Exchange getOneExchange(UserDTO userDTO, Long id) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        if(user.getId()==exchange.getUser().getId()){
            return exchange;
        }
        else {
            return null;
        }
    }


    public ExchangeDTO convertToDTO(Exchange exchange) {
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setId(exchange.getId());
        exchangeDTO.setExchangeDate(exchange.getExchange_date());
        exchangeDTO.setPaymentAmount(exchange.getPayment_amount());
        exchangeDTO.setStatus(exchange.getStatus());
        exchangeDTO.setDescription(exchange.getDescription());
        User user = exchange.getUser();
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

                    PostDTO postDTO = new PostDTO(post.getId(), post.getImage(), post.getContent(), post.getCreate_date(), post.getTotal_like(), post.getTotal_comment(), commentService.convertCommentListToDTO(post.getComments()), petToPostDTO, userPostDTO,false);
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
        UserDTO userDTO = new UserDTO(user.getId(), user.getName(),user.getEmail(),user.getPhone(),user.getAvatar(), user.getRole(), petDTOList,postDTOList);
        exchangeDTO.setUserDTO(userDTO);
        Pet pet = exchange.getPet();
        PetDTO petDTO = new PetDTO(pet.getId(),pet.getImage(),pet.getName(),pet.getDescription());
        exchangeDTO.setPetDTO(petDTO);
        return exchangeDTO;
    }

}
