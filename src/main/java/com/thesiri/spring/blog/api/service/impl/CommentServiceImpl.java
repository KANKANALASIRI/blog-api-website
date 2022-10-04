package com.thesiri.spring.blog.api.service.impl;

import com.thesiri.spring.blog.api.entity.Comment;
import com.thesiri.spring.blog.api.entity.Post;
import com.thesiri.spring.blog.api.exception.BlogAPIException;
import com.thesiri.spring.blog.api.exception.ResourceNotFoundException;
import com.thesiri.spring.blog.api.payload.CommentDto;
import com.thesiri.spring.blog.api.repository.CommentRepository;
import com.thesiri.spring.blog.api.repository.PostRepository;
import com.thesiri.spring.blog.api.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl  implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    //Created ModelMapper
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // retrieve post by id
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
//                .orElseThrow(() -> new RuntimeException("resource not found"));


        // set post to comment entity
        comment.setPost(post);
        // save comment to database
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        // retrieve comment by postId - create a custom method in repository
        List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment to list of commentDto
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

//                .orElseThrow(() -> new RuntimeException("resource not found"));
        // retrieve comment by id
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
//                .orElseThrow(() -> new RuntimeException("resource not found"));

        if (!comment.getPost().getId().equals(post.getId())) {

            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//            throw new RuntimeException("comment does not belong to post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentUpdate) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

//                .orElseThrow(() -> new RuntimeException("resource not found"));
        // retrieve comment by id
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
//                .orElseThrow(() -> new RuntimeException("resource not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//            throw new RuntimeException("comment does not belong to post");
        }

        comment.setName(commentUpdate.getName());
        comment.setEmail(commentUpdate.getEmail());
        comment.setBody(commentUpdate.getBody());

        Comment commentUpdated = commentRepository.save(comment);
        return mapToDTO(commentUpdated);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        // retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
//                .orElseThrow(() -> new RuntimeException("resource not found"));
        // retrieve comment by id
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
//                .orElseThrow(() -> new RuntimeException("resource not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
//            throw new RuntimeException("comment does not belong to post");
        }

        commentRepository.delete(comment);

    }

    // convert Entity to DTO
    private CommentDto mapToDTO(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    // convert DTO to Entity
    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }
}
