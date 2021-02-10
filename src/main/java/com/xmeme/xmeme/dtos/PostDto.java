package com.xmeme.xmeme.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    
    private String id;

    private String name;

    private String url;

    private String caption;
}
