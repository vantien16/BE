package com.petlover.petsocial.controller;

import com.petlover.petsocial.payload.request.CommentDTO;
import com.petlover.petsocial.service.CommentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        CommentDTO commentDTO = commentService.getCommentById(id);
        if (commentDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        return new ResponseEntity<>(commentDTO, HttpStatus.OK);
    }


    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDTO> commentDTOs = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }


    @PostMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long userId, @PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        CommentDTO savedComment = commentService.createComment(userId, postId, commentDTO);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentDTO comment) {
        CommentDTO updatedComment = commentService.updateComment(id, comment);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
