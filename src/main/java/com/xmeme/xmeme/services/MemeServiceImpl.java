package com.xmeme.xmeme.services;

import java.util.List;
import java.util.Map;
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

/**
 * Service class that implements the MemeService contract. 
 */

@Service
public class MemeServiceImpl implements MemeService{
    
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    private ModelMapper mapper = new ModelMapper();

    /**
     * This function gets 100 most recent meme post entites by calling RepositoryService.
     * @return List<PostDto>
     */
    public List<PostDto> getPosts(){

        List<PostEntity> postEntities = repositoryService.getPosts();

        List<PostDto> posts = new ArrayList<>();

        for (PostEntity post:postEntities) {
            posts.add(mapper.map(post, PostDto.class));
        }

        return posts;
    }

    /**
     * This function gets a single instance of PostEntity by calling the RepositoryService
     * @param long postId
     * @return A single PostDto object
     * @throws PostNotFoundException if RepositoryService did not find a post with the given postId in the database
     */
    public PostDto getPost(long postId) throws PostNotFoundException{
        PostEntity postEntity = repositoryService.getPost(postId);
        return mapper.map(postEntity, PostDto.class);
    }

    /**
     * This function creates PostEntity from a PostDto and calls RepositoryService to save the PostEntity into the database.
     * Before calling the RepositoryService it first validates all the fields in the provided PostDto
     * @param PostDto post
     * @return long postId - The id of the saved PostEntity
     * @throws InvalidPostException if the validation of the PostDto fails
     */
    public long savePost(PostDto post) throws InvalidPostException {

        validatePost(post);

        PostEntity entity = PostEntity.builder()
                                .id(sequenceGenerator.generateSequence(PostEntity.SEQUENCE_NAME))
                                .name(post.getName())
                                .url(post.getUrl())
                                .caption(post.getCaption())
                                .dateOfPosting(LocalDate.now())
                                .build();
        long postId = repositoryService.savePost(entity);
        return postId;
    }


    /**
     * Function to update an existing PostEntity with a given id in the database. 
     * @param Map<String, Object> updates
     * @param long postId
     * @throws PostNotFoundException - If the post with the given id for update is not found.
     * @throws InvalidPostException - If the updates are invalid
     */
    public void updatePost(Map<String, Object> updates, long postId) throws PostNotFoundException, InvalidPostException{

        Object newUrl = updates.get("url");

        Object newCaption = updates.get("caption");

        if (newUrl != null && isImageUrl(newUrl.toString()) == false) {
            throw new InvalidPostException("Update failed because upadated url is not a valid image url.");
        }

        if (newCaption != null && newCaption.toString().isBlank()) {
            throw new InvalidPostException("Update failed because updated caption is blank.");
        }

        repositoryService.updatePost(updates, postId); 
    }

    /**
     * Function to validate the fields of a PostDto
     * @param post
     * @throws InvalidPostException
     */
    private void validatePost(PostDto post) throws InvalidPostException{

        String name = post.getName();
        String url = post.getUrl();
        String caption = post.getCaption();

        if (name == null || name.isBlank()){
            throw new InvalidPostException("The name cannot be empty");
        }

        if (url == null || url.isBlank()){
            throw new InvalidPostException("The url cannot be empty");
        }

        if (caption == null || caption.isBlank()) {
            throw new InvalidPostException("The caption cannot be empty.");
        }

        if (isImageUrl(url) == false) {
            throw new InvalidPostException("The url is not a valid image url.");
        }
    }


    /**
     * Function to check if the provided url contains an image
     * @param uri
     * @return true if a url is a valid image url or else false
     */
    private boolean isImageUrl(String uri) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                                            .uri(URI.create(uri))
                                            .GET()
                                            .build();
            
            HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());

            String contentType = response.headers().allValues("content-type").get(0);
            
            if (contentType.equals("image/jpeg") || contentType.equals("image/jpg") || contentType.equals("image/png")
                || contentType.equals("image/JPEG") || contentType.equals("image/JPG") || contentType.equals("image/PNG")) return true;
            
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
