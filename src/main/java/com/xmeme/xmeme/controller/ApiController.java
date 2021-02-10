package com.xmeme.xmeme.controller;

import java.util.List;

import com.xmeme.xmeme.dtos.PostDto;
import com.xmeme.xmeme.exceptions.InvalidPostException;
import com.xmeme.xmeme.exceptions.PostNotFoundException;
import com.xmeme.xmeme.exchanges.SavePostResponse;
import com.xmeme.xmeme.services.MemeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    
    @Autowired
    private MemeService memeService;

    public static final String BASE_URL = "/memes";

    @GetMapping(BASE_URL)
    public List<PostDto> getPosts() {
        return memeService.getPosts();
    }

    @GetMapping(BASE_URL+"/{id}")
    public PostDto getPost(@PathVariable long id) throws PostNotFoundException {
        return memeService.getPost(id);
    }

    @PostMapping(BASE_URL)
    public SavePostResponse savePost(@RequestBody PostDto post) throws InvalidPostException{
        long postId = memeService.savePost(post);
        SavePostResponse response = new SavePostResponse(String.valueOf(postId));
        return response;
    }
}
