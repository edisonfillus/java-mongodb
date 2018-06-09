package org.project.example.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static org.project.example.util.Helper.printJson;

public class DeleteExample {

    public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("course");
            MongoCollection<Document> collection = database.getCollection("test");

            collection.drop();

            // insert 8 documents, with _id set to the value of the loop variable
            for (int i = 0; i < 8; i++) {
                collection.insertOne(new Document().append("_id", i));
            }

            // Delete all that match
            //collection.deleteMany(gt("_id",4));

            // Delete the first match found
            collection.deleteOne(eq("_id", 4));

            for (Document cur : collection.find().into(new ArrayList<Document>())) {
                printJson(cur);
            }
        }
    }
}
