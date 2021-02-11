package com.xmeme.xmeme.repositoryservice;

import java.util.List;
import java.util.Map;

import com.xmeme.xmeme.entities.PostEntity;
import com.xmeme.xmeme.exceptions.PostNotFoundException;

public interface RepositoryService {
    
    List<PostEntity> getPosts();

    PostEntity getPost(long postId) throws PostNotFoundException;

    long savePost(PostEntity post);

    void updatePost(Map<String, Object> updates, long postId) throws PostNotFoundException;
}
