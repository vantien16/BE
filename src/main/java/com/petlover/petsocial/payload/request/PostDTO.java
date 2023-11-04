package com.petlover.petsocial.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {
    private Long id;
    private String image;
    private String content;
    private String create_date;
    private int total_like;
    private int total_comment;
    private List<CommentDTO> comments;
    private PetToPostDTO petToPostDTO;
    private UserPostDTO userPostDTO;
    private boolean fieldReaction;

}
