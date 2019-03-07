package com.dyny.basemongodb.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

/**
 * @Auther: lane
 * @Date: 2019-03-07 15:44
 * @Description:
 * @Version 1.0.0
 */
@Configuration
public class MongoDBConfigurator {
    @Autowired
    MongoDbFactory mongoDbFactory;

    @Bean
    public GridFSBucket getGridFSBucket() {
        MongoDatabase mongoDatabase = mongoDbFactory.getDb();
        return GridFSBuckets.create(mongoDatabase);
    }
}
