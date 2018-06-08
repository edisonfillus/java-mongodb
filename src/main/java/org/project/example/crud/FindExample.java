package org.project.example.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static org.project.example.util.Helper.printJson;

public class FindExample {
    public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("course");
            MongoCollection<Document> collection = database.getCollection("findTest");

            collection.drop();

            // insert 10 documents
            for (int i = 0; i < 10; i++) {
                collection.insertOne(new Document("x", i));
            }

            System.out.println("Find one:");
            Document first = collection.find().first();
            printJson(first);

            System.out.println("Find all with into: ");
            List<Document> all = collection.find().into(new ArrayList<>());
            for (Document cur : all) {
                printJson(cur);
            }

            System.out.println("Find all with iteration: ");
            MongoCursor<Document> cursor = collection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    Document cur = cursor.next();
                    printJson(cur);
                }
            } finally {
                cursor.close();
            }

            System.out.println("Count:");
            long count = collection.count();
            System.out.println(count);
        }

    }
}
