package com.dyny.basemongodb.dao;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * @Auther: lane
 * @Date: 2019-04-03 11:22
 * @Description:
 * @Version 1.0.0
 */
public interface MongodbDao {
    String KEY_ID = "_id";


    public String insert(String tableName, String jsonData);

    public String insert(String tableName, String id, String jsonData);


    public String insert(String tableName, Document data);


    public boolean delete(String tableName, String id);


    public Document find(String tableName, String id);


    public MongoCursor<Document> find(String tableName, Bson filter);


    public Document update(String tableName, String id, Document data);

    public Document update(String tableName, String id, String data);

    /**
     * 分页查询
     */
    public MongoCursor<Document> findByPage(String tableName, Bson filter, int pageNo, int pageSize);
}
