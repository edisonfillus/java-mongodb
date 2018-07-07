package org.project.example.replication;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static java.util.Arrays.asList;

public class WriteConcernTest {
    public static void main(String[] args) {

        // WriteConcern.W1: At least one node should commit in memory
        // WriteConcern.MAJORITY: More than half of nodes should commit in memory
        // WriteConcern.JOURNALED: Should write to disk, not just memory. Failsafe, but slower

        MongoClient client = new MongoClient(asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
        ), MongoClientOptions.builder().writeConcern(WriteConcern.JOURNALED).build());


        // Preference can be set specific by connection/collection
        MongoDatabase database = client.getDatabase("course")
                .withWriteConcern(WriteConcern.W2);

        // Preference can be set specific by connection/collection
        MongoCollection<Document> collection = database.withWriteConcern(WriteConcern.MAJORITY)
                .getCollection("replication");

        collection.drop();

        Document doc = new Document("_id", 1);

        collection.insertOne(doc);

        try {
            collection.withWriteConcern(WriteConcern.UNACKNOWLEDGED).insertOne(doc);
        } catch (DuplicateKeyException e) {
            System.out.println(e.getMessage());
        }

    }
}
