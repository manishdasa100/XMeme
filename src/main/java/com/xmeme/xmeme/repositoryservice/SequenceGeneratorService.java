package com.xmeme.xmeme.repositoryservice;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Objects;

import com.xmeme.xmeme.entities.DatabaseSequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public long generateSequence(String seqName) {

        Query query = new Query(Criteria.where("id").is(seqName));
        DatabaseSequence counter = mongoTemplate.findAndModify(query, new Update().inc("sequenceNo", 1), FindAndModifyOptions.options().returnNew(true).upsert(true), DatabaseSequence.class, "database_sequences");
        return !Objects.isNull(counter) ? counter.getSequenceNo() : 1;
    }

}
