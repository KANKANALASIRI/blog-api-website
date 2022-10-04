package com.thesiri.spring.blog.api.controller;

import com.thesiri.spring.blog.api.payload.CommentDto;
import com.thesiri.spring.blog.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") Long postId,
                                                    @RequestBody CommentDto commentDto) {
        CommentDto data = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentDto> data = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") Long postId,
                                                     @PathVariable("id") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("postId") Long postId,
                                                    @PathVariable("id") Long commentId,
                                                    @RequestBody CommentDto commentDto){
        CommentDto updateComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updateComment, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") Long postId,
                                                @PathVariable("id") Long commentId){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("comment deleted successfully", HttpStatus.OK);
    }
}
