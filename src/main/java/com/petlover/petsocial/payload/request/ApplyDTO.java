package com.petlover.petsocial.payload.request;

import com.petlover.petsocial.model.entity.*;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.websocket.utils.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplyDTO {
    private Long id;
    private Date applyDate;
    private ApplyStatus status;
    private ExchangeDTO exchange;
    private UserAppliedForExchangeDTO userApplied;
    public static ApplyDTO convertToDTO(Apply apply) {
        ApplyDTO applyDTO = new ApplyDTO();
        applyDTO.setId(apply.getId());
        applyDTO.setApplyDate(apply.getApply_date());
        applyDTO.setStatus(apply.getStatus());
        applyDTO.setExchange(convertToDTO1(apply.getExchange()));

        // Assuming you have appropriate getters in the Exchange and User entities
        applyDTO.setUserApplied(new UserAppliedForExchangeDTO(apply.getUser().getId(),apply.getUser().getName(),apply.getUser().getEmail(),apply.getUser().getPhone(),apply.getUser().getAvatar()));

        return applyDTO;
    }




    public static ExchangeDTO convertToDTO1(Exchange exchange) {
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setId(exchange.getId());
        exchangeDTO.setExchangeDate(exchange.getExchange_date());
        exchangeDTO.setPaymentAmount(exchange.getPayment_amount());
        exchangeDTO.setStatus(exchange.getStatus());
        exchangeDTO.setDescription(exchange.getDescription());
        User user = exchange.getUser();
        List<PostDTO> postDTOList = new ArrayList<>();

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


