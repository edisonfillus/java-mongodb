package org.project.example.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

import static org.project.example.util.Helper.printJson;

public class InsertExample {
    public static void main(String[] args) {
        MongoDatabase db;
        try (MongoClient client = new MongoClient()) {
            db = client.getDatabase("course");
            MongoCollection<Document> coll = db.getCollection("insertTest");

            coll.drop();

            Document smith = new Document("name", "Smith")
                    .append("age", 30)
                    .append("profession", "programmer");

            Document jones = new Document("name", "Jones")
                    .append("age", 25)
                    .append("profession", "hacker");

            printJson(smith);

            coll.insertOne(smith);
            coll.insertOne(jones);

            printJson(smith);
            printJson(jones);

            coll.drop();

            coll.insertMany(Arrays.asList(smith, jones));

            printJson(smith);
            printJson(jones);
        }

    }
}
