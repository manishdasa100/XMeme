package com.xmeme.xmeme.repositoryservice;

import java.util.List;

import com.xmeme.xmeme.entities.PostEntity;

public interface RepositoryService {
    
    List<PostEntity> getPosts();

    PostEntity getPost(long postId);

    long savePost(PostEntity post);
}
