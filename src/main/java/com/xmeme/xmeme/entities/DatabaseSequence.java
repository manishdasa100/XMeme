package com.xmeme.xmeme.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "database_sequences")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatabaseSequence {
    
    @Id
    private String id;

    private long sequenceNo;
}
