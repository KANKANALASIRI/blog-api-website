package com.thesiri.spring.blog.api.service;

import com.thesiri.spring.blog.api.payload.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
     CommentDto createComment(Long postId, CommentDto commentDto);
     List<CommentDto> getCommentsByPostId(Long postId);
     CommentDto getCommentById(Long postId,Long commentId);
     CommentDto updateComment(Long postId, Long commentId, CommentDto commentUpdate);
     void deleteComment(Long postId, Long commentId);
}
