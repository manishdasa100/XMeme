package com.xmeme.xmeme.repositoryservice;

import java.util.Collections;
import java.util.List;

import com.xmeme.xmeme.entities.PostEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class RepositoryServiceImpl implements RepositoryService{
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PostEntity> getPosts() {

        // Query to get the bottom 100 documents from the mongodb collection.
        Query query = new Query().with(Sort.by(Direction.DESC, "id")).limit(100);
        
        List<PostEntity> posts = mongoTemplate.find(query, PostEntity.class, "userPosts");
        
        Collections.reverse(posts);

        return posts;
    }

    @Override
    public PostEntity getPost(long postId) {
        Query query = new Query(Criteria.where("id").is(postId));
        PostEntity post = mongoTemplate.findOne(query, PostEntity.class, "userPosts");
        return post;
    }

    @Override
    public long savePost(PostEntity post) {
        PostEntity savedPost = mongoTemplate.save(post, "userPosts");
        return savedPost.getId();
    }
}
