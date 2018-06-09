package org.project.example.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static org.project.example.util.Helper.printJson;

public class FindWithSortSkipLimitExample {
    public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("course");
            MongoCollection<Document> collection = database.getCollection("findWithSortTest");

            collection.drop();

            // insert 100 documents with two random integers
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    collection.insertOne(new Document().append("i", i).append("j", j));
                }
            }

            Bson projection = fields(include("i", "j"), excludeId());

            //Bson sort = new Document("i", 1).append("j", -1);
            Bson sort = orderBy(ascending("i"),descending("j", "i"));

            List<Document> all = collection.find()
                    .projection(projection)
                    .sort(sort)
                    .skip(20)
                    .limit(50)
                    .into(new ArrayList<>());

            for (Document cur : all) {
                printJson(cur);
            }
        }

    }

}
