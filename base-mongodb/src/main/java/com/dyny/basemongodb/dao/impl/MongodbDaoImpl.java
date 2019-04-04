package com.dyny.basemongodb.dao.impl;

import com.dyny.basemongodb.dao.MongodbDao;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Repository;

/**
 * @Auther: lane
 * @Date: 2019-04-03 11:22
 * @Description:
 * @Version 1.0.0
 */
@Repository("mongodbDao")
public class MongodbDaoImpl implements MongodbDao {
    @Autowired
    private MongoDbFactory mongoDbFactory;


    public String insert(String tableName, String jsonData) {
        return insert(tableName, Document.parse(jsonData));
    }

    @Override
    public String insert(String tableName, String id, String jsonData) {
        Document document = Document.parse(jsonData);
        if (StringUtils.isNotEmpty(id)) {
            document.append(KEY_ID, new ObjectId(id));
        }
        return insert(tableName, document);
    }

    @Override
    public String insert(String tableName, Document data) {
        MongoCollection mongoCollection = this.mongoDbFactory.getDb().getCollection(tableName);
        mongoCollection.insertOne(data);
        return data.getObjectId(KEY_ID).toString();

    }

    @Override
    public boolean delete(String tableName, String id) {
        int count;
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (Exception e) {
            return false;
        }
        Bson filter = Filters.eq(KEY_ID, objectId);
        MongoCollection<Document> collection = this.mongoDbFactory.getDb().getCollection(tableName);
        DeleteResult deleteResult = collection.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count > 0;
    }

    @Override
    public Document find(String tableName, String id) {
        MongoCollection<Document> collection = this.mongoDbFactory.getDb().getCollection(tableName);
        return collection.find(Filters.eq(KEY_ID, new ObjectId(id))).first();
    }


    @Override
    public MongoCursor<Document> findByPage(String tableName, Bson filter, int pageNo, int pageSize) {
        MongoCollection<Document> collection = this.mongoDbFactory.getDb().getCollection(tableName);
        Bson orderBy = new BasicDBObject(KEY_ID, 1);
        return collection.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    @Override
    public MongoCursor<Document> find(String tableName, Bson filter) {
        MongoCollection<Document> collection = this.mongoDbFactory.getDb().getCollection(tableName);
        return collection.find(filter).iterator();
    }

    @Override
    public Document update(String tableName, String id, Document data) {
        MongoCollection<Document> collection = this.mongoDbFactory.getDb().getCollection(tableName);
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq(KEY_ID, objectId);
        collection.replaceOne(filter, data);
        return collection.find(Filters.eq(KEY_ID, new ObjectId(id))).first();
    }

    @Override
    public Document update(String tableName, String id, String data) {
        return update(tableName, id, Document.parse(data));
    }

}
