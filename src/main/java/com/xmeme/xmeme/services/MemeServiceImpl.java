package com.xmeme.xmeme.services;

import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;

import com.xmeme.xmeme.dtos.PostDto;
import com.xmeme.xmeme.entities.PostEntity;
import com.xmeme.xmeme.exceptions.InvalidPostException;
import com.xmeme.xmeme.exceptions.PostNotFoundException;
import com.xmeme.xmeme.repositoryservice.RepositoryService;
import com.xmeme.xmeme.repositoryservice.SequenceGeneratorService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemeServiceImpl implements MemeService{
    
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    private ModelMapper mapper = new ModelMapper();

    public List<PostDto> getPosts(){

        List<PostEntity> postEntities = repositoryService.getPosts();

        List<PostDto> posts = new ArrayList<>();

        for (PostEntity post:postEntities) {
            posts.add(mapper.map(post, PostDto.class));
        }

        return posts;
    }

    public PostDto getPost(long postId) throws PostNotFoundException{
        PostEntity postEntity = repositoryService.getPost(postId);
        if (postEntity == null) throw new PostNotFoundException();
        return mapper.map(postEntity, PostDto.class);
    }

    public long savePost(PostDto post) throws InvalidPostException {

        String name = post.getName();
        String url = post.getUrl();
        String caption = post.getCaption();

        if (name == null || name.isEmpty() || url == null || url.isEmpty() || caption == null || caption.isEmpty()) {
            throw new InvalidPostException();
        }

        if (isImageUrl(url) == false) {
            throw new InvalidPostException("The url is not a valid image url.");
        }

        PostEntity entity = PostEntity.builder()
                                .id(sequenceGenerator.generateSequence(PostEntity.SEQUENCE_NAME))
                                .name(name)
                                .url(url)
                                .caption(caption)
                                .dateOfPosting(LocalDate.now())
                                .build();
        long postId = repositoryService.savePost(entity);
        return postId;
    }

    private boolean isImageUrl(String uri) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                            .uri(URI.create(uri))
                                            .GET()
                                            .build();
            
            HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());

            String contentType = response.headers().allValues("content-type").get(0);
            
            if (contentType.equals("image/jpeg") || contentType.equals("image/jpg") || contentType.equals("image/png")) return true;
            
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
