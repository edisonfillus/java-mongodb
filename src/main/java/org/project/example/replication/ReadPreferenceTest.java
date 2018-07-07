package org.project.example.replication;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ReadPreferenceTest {
    public static void main(String[] args) {

        // Set all nodes in ReplicaSet
        MongoClient client = new MongoClient(asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019))
        );

        // Set the preference to read only the secondaries
        MongoDatabase database = client.getDatabase("course").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> collection = database.getCollection("replication");

        // It's possible to set the preference on the query.
        List<Document> documents = collection.withReadPreference(ReadPreference.primaryPreferred())
                .find().into(new ArrayList<>());

        documents.forEach(doc -> System.out.println(doc.toJson()));
    }
}
