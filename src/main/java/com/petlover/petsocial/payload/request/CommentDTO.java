package com.petlover.petsocial.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO userDTO;
    private Long postId;
    @CreationTimestamp
    private LocalDateTime createdTime;
}
