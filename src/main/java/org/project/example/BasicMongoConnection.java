package org.project.example;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static org.project.example.util.Helper.printJson;

public class BasicMongoConnection {

    public static void main(String[] args) {

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(5).build();
        MongoDatabase db;
        try (MongoClient client = new MongoClient(new ServerAddress(), options)) {
            db = client.getDatabase("admin");
            MongoCollection<Document> col = db.getCollection("system.version");
            Document doc = col.find().first();
            printJson(doc);
        }

    }

}
