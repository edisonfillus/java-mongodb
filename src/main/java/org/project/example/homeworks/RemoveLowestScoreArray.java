package org.project.example.homeworks;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.pull;

public class RemoveLowestScoreArray {

    public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("school");
            MongoCollection<Document> collection = database.getCollection("students");

            // Get all students
            List<Document> students = collection.find().into(new ArrayList<>());

            for (Document student : students) {
                Document lowestScore = null;
                for (Document score : (List<Document>) student.get("scores")) {
                    if (score.getString("type").equals("homework")) {
                        if (lowestScore == null || score.getDouble("score") < (lowestScore.getDouble("score"))) {
                            lowestScore = score;
                        }
                    }
                }
                collection.updateOne(student, pull("scores", lowestScore));
            }
        }
    }


}
