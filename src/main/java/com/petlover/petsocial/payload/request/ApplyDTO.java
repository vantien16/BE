package com.petlover.petsocial.payload.request;

import com.petlover.petsocial.model.entity.Apply;
import com.petlover.petsocial.model.entity.ApplyStatus;
import com.petlover.petsocial.model.entity.Exchange;
import com.petlover.petsocial.model.entity.Pet;
import com.petlover.petsocial.model.entity.Post;
import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.service.CommentService;
import com.petlover.petsocial.service.ExchangeService;
import com.petlover.petsocial.websocket.utils.MapperUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplyDTO {

  private Long id;
  private Date applyDate;
  private ApplyStatus status;
  private ExchangeDTO exchangeDTO;
  private Long userId;
  private UserDTO userDTO;
  @Autowired
  private static CommentService commentService;
  @Autowired
  private  static ExchangeService exchangeService;

  public  static ApplyDTO convertToDTO(Apply apply) {
    ApplyDTO applyDTO = new ApplyDTO();
    applyDTO.setId(apply.getId());
    applyDTO.setApplyDate(apply.getApply_date());
    applyDTO.setStatus(apply.getStatus());

    //Chyen user sang userDTO ne nha
    User user = apply.getUser();
    List<PostDTO> postDTOList = new ArrayList<>();
    for (Post post : user.getPosts()) {
      if (post.isStatus() == true) {
        if (post.isEnable() == true) {
          PetToPostDTO petToPostDTO = new PetToPostDTO();
          petToPostDTO.setId(post.getPet().getId());
          petToPostDTO.setName(post.getPet().getName());
          petToPostDTO.setImage(post.getPet().getImage());

          UserPostDTO userPostDTO = new UserPostDTO();
          userPostDTO.setId(post.getUser().getId());
          userPostDTO.setName(post.getUser().getName());
          userPostDTO.setAvatar(post.getUser().getAvatar());

          PostDTO postDTO = new PostDTO(post.getId(), post.getImage(), post.getContent(),
              post.getCreate_date(), post.getTotal_like(), post.getTotal_comment(),
              commentService.convertCommentListToDTO(post.getComments()), petToPostDTO, userPostDTO,
              false);
          postDTOList.add(postDTO);
        }
      }
    }

    List<PetDTO> petDTOList = new ArrayList<>();
    for (Pet pet : user.getPets()) {
      if (pet.isStatus() == true) {
        PetDTO petDTO = new PetDTO(pet.getId(), pet.getImage(), pet.getName(),
            pet.getDescription());
        petDTOList.add(petDTO);
      }
    }
    UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(),
        user.getAvatar(), user.getRole(), petDTOList, postDTOList);
    applyDTO.setUserDTO(userDTO);
    //

    //Chueyn ExchangeDTO nha may huynh
    applyDTO.setExchangeDTO(exchangeService.getOneExchangeDTO(applyDTO.getUserDTO(),apply.getExchange().getId()));
    //

    //

    return applyDTO;
  }

}


