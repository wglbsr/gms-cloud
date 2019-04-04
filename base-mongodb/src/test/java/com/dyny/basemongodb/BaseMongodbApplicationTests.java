package com.dyny.basemongodb;

import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseMongodbApplicationTests {
    @Autowired
    MongoDbFactory mongoDbFactory;

    @Test
    public void contextLoads() {
        MongoDatabase mongoDatabase = mongoDbFactory.getDb();
        ListCollectionsIterable<Document> result = mongoDatabase.listCollections();
        MongoCollection resulr1 = mongoDatabase.getCollection("fs");
    }

}
