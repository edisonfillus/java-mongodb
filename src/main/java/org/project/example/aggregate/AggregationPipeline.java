package org.project.example.aggregate;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.gte;
import static org.bson.Document.parse;

public class AggregationPipeline {
    public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("course");
            MongoCollection<Document> collection = database.getCollection("zipcodes");

            /*
            db.zipcodes.aggregate([
                {
                    $group: {
                        _id: "$state",
                         totalPop: {$sum: "$pop"}
                    }
                },
                {
                    $match: {
                        totalPop: { $gte: 1000000 }
                     }
                }
            ]);
             */


            // Can be done with Strings
            List<Document> pipeline = Arrays.asList(
                    parse("{$group: {_id: '$state', totalPop: {$sum: '$pop'}}}"),
                    parse("{$match: { totalPop: { $gte: 1000000 }}}")
            );




            // Can be done with raw Documents
            List<Document> pipeline1 = Arrays.asList(
                    new Document("$group",
                            new Document()
                                    .append("_id", "$state")
                                    .append("totalPop", new Document("$sum", "$pop"))
                    ),
                    new Document("$match",
                            new Document()
                                    .append("totalPop", new Document("$gte", 10000000))
                    )
            );

            // Can be done using builders
            List<Bson> pipeline2 = Arrays.asList(
                    group(
                            "$state",
                            sum("totalPop","$pop")
                    ),
                    match(
                            gte("totalPop", 10000000)
                    )
            );


            List<Document> results = collection.aggregate(pipeline).into(new ArrayList<>());
            for (Document cur : results) {
                System.out.println(cur.toJson());
            }

        }

    }
}
