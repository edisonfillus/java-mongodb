package org.project.example.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.inc;
import static org.project.example.util.Helper.printJson;

public class UpdateExample {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("course");
        MongoCollection<Document> collection = database.getCollection("test");

        collection.drop();

        // insert 8 documents, with both _id and x set to the value of the loop variable
        for (int i = 0; i < 8; i++) {
            collection.insertOne(new Document().append("_id", i)
                    .append("x", i)
                    .append("y", true));
        }

        // Replace the entire document that match the filter, using raw Document
        // collection.replaceOne(eq("x", 5), new Document().append("x", 20).append("updated", true));

        // Update the document that match the filter, using raw Document
        //collection.updateOne(eq("x", 5), new Document("$set", new Document("x", 20).append("updated", true)));

        // Update the document that match the filter, using functions
        //collection.updateOne(eq("x", 5), combine(set("x", 20), set("updated", true)));

        // Update the document that match the filter, if there is no match, insert the document
        //collection.updateOne(eq("x", 9), combine(set("x", 20), set("updated", true)), new UpdateOptions().upsert(true));

        // Update all the documents that match the filter
        collection.updateMany(gte("x", 5), inc("x", 1));


        for (Document cur : collection.find().into(new ArrayList<Document>())) {
            printJson(cur);
        }
    }
}
