package com.xmeme.xmeme.services;

import java.util.List;
import java.util.Map;

import com.xmeme.xmeme.dtos.PostDto;
import com.xmeme.xmeme.exceptions.InvalidPostException;
import com.xmeme.xmeme.exceptions.PostNotFoundException;

public interface MemeService {
    
    List<PostDto> getPosts();

    PostDto getPost(long postId) throws PostNotFoundException;

    long savePost(PostDto post) throws InvalidPostException;

    void updatePost(Map<String, Object> updates, long postId) throws PostNotFoundException, InvalidPostException;
}
