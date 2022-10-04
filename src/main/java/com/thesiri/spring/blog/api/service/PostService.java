package com.thesiri.spring.blog.api.service;

import com.thesiri.spring.blog.api.payload.PostDto;
import com.thesiri.spring.blog.api.payload.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    PostDto createPost(PostDto postDto);

//    List<PostDto> getAllPosts();
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long id);
    PostDto updatePost(PostDto postDto, Long id);
    void deletePostById(Long id);
}
