package com.thesiri.spring.blog.api.service.impl;

import com.thesiri.spring.blog.api.entity.Post;
import com.thesiri.spring.blog.api.payload.PostDto;
import com.thesiri.spring.blog.api.payload.PostResponse;
import com.thesiri.spring.blog.api.repository.PostRepository;
import com.thesiri.spring.blog.api.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    // create modelMapper
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostDto postDto) {
        // convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        // convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

//    @Override
//    public List<PostDto> getAllPosts() {
//        List<Post> postList = postRepository.findAll();
//        return postList.stream().map(pos -> mapToDTO(pos)).collect(Collectors.toList());
//    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> postList = posts.getContent();

        List<PostDto> content = postList.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }


    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("resource not found"));

        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        // get post by id from database
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("resource not found"));
        // get update data
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        // save the changes
        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(Long id) {
        // get post by id from database
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("resource not found"));
        // delete the post
        postRepository.delete(post);
    }

    // convert Entity to DTO
    private PostDto mapToDTO(Post post) {

        PostDto postDto = modelMapper.map(post, PostDto.class);
        // not using modelMapper for conversion
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    // convert DTP to Entity
    private Post mapToEntity(PostDto postDto) {

        Post post = modelMapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
