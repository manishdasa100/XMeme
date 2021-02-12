package com.xmeme.xmeme.controller;

import java.util.List;
import java.util.Map;

import com.xmeme.xmeme.dtos.PostDto;
import com.xmeme.xmeme.exceptions.InvalidPostException;
import com.xmeme.xmeme.exceptions.PostNotFoundException;
import com.xmeme.xmeme.exchanges.SavePostResponse;
import com.xmeme.xmeme.services.MemeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
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
    public PostDto getPost(@PathVariable(name = "id") long id) throws PostNotFoundException {
        return memeService.getPost(id);
    }

    @PostMapping(BASE_URL)
    public SavePostResponse savePost(@RequestBody PostDto post) throws InvalidPostException{
        long postId = memeService.savePost(post);
        SavePostResponse response = new SavePostResponse(String.valueOf(postId));
        return response;
    }

    @PatchMapping(BASE_URL+"/{id}")
    public ResponseEntity<?> updatePost(@RequestBody Map<String, Object> updates, @PathVariable(name = "id") long id) throws PostNotFoundException, InvalidPostException{
        memeService.updatePost(updates, id);
        return ResponseEntity.ok("Post updated");
    }
}
