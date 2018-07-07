package org.project.example.replication;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static java.util.Arrays.asList;

public class ReplicaSetTest {

    public static void main(String[] args) throws InterruptedException {

        // Set all nodes in ReplicaSet, defines that the name must be 'replset'
        MongoClient client = new MongoClient(asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
        ), MongoClientOptions.builder().requiredReplicaSetName("replset").build());

        MongoCollection<Document> collection = client.getDatabase("course")
                .getCollection("replication");

        collection.drop();

        // Loop almost forever for failover test
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            collection.insertOne(new Document("_id", i));
            System.out.println("Inserted document: " + i);
            Thread.sleep(500);
        }
    }
}
