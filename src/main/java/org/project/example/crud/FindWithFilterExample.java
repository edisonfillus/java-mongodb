package org.project.example.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static org.project.example.util.Helper.printJson;

public class FindWithFilterExample {

    public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("course");
            MongoCollection<Document> collection = database.getCollection("findWithFilterTest");

            collection.drop();

            // insert 10 documents with two random integers
            for (int i = 0; i < 10; i++) {
                collection.insertOne(new Document()
                        .append("x", new Random().nextInt(2))
                        .append("y", new Random().nextInt(100)));
            }

            //Can be done this way with raw document
            //Bson filter = new Document("x", 0).append("y", new Document("$gt", 10).append("$lt", 90));

            //Or this way, with functions. It's better because it's possible to use more advanced filters
            Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 90));

            List<Document> all = collection.find(filter).into(new ArrayList<Document>());

            for (Document cur : all) {
                printJson(cur);
            }

            long count = collection.count(filter);
            System.out.println();
            System.out.println(count);
        }

    }

}
