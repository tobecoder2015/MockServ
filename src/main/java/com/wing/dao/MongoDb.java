package com.wing.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import org.bson.BsonDocument;
import org.bson.Document;


public class MongoDb {
    protected MongoClient mongoClient;

    /**
     * Default constructor
     */
    public MongoDb() {

    }

    public void init(String url) {
        if (mongoClient == null) {
            mongoClient = new MongoClient(new MongoClientURI(url));
        }
    }

    /**
     * Constructor
     *
     * @param url
     */
    public MongoDb(String url) {
        mongoClient = new MongoClient(new MongoClientURI(url));
    }

    /**
     * Find mongo document
     *
     * @param databaseName
     * @param collectionName
     * @param queryJson
     * @return
     */
    public FindIterable<Document> find(String databaseName, String collectionName, String queryJson) {
        return mongoClient.getDatabase(databaseName).getCollection(collectionName).find(BsonDocument.parse(queryJson));
    }


    /**
     * Find first doument
     *
     * @param databaseName
     * @param collectionName
     * @param queryJson
     * @return
     */
    public Document findFirst(String databaseName, String collectionName, String queryJson) {
        FindIterable<Document> iterator = find(databaseName, collectionName, queryJson);
        if (iterator != null) {
            return iterator.first();
        }
        return null;
    }
}
