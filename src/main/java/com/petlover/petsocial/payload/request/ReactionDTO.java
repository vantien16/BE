package com.petlover.petsocial.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReactionDTO {
    private Long id;
    private boolean isActive;
    private Long userId;
    private Long postId;
    private Long commentId;

    public ReactionDTO(Long id, Long userId, Long postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }
}
