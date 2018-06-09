package org.project.example.homeworks;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static org.project.example.util.Helper.printJson;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class RemoveLowestScore {
	
	public static void main(String[] args) {
        MongoDatabase database;
        try (MongoClient client = new MongoClient()) {
            database = client.getDatabase("students");
            MongoCollection<Document> collection = database.getCollection("grades");

            Bson filter = eq("type","homework");
            
            Bson sort = orderBy(ascending("student_id"),descending("score"));
            
            List<Document> students = collection.find(filter).sort(sort).into(new ArrayList<Document>());
            
            Document lastDocument = students.get(0);
            
            for (int i = 1; i < students.size(); i++) {
            	Document student = students.get(i);
            	
            	if(student.getInteger("student_id").equals(lastDocument.getInteger("student_id"))) {
            		//Check if it is the last one, if true, delete
            		if (i == students.size()-1) {
            			collection.deleteOne(student);
            		} else {
            			//If same student, just update lastDocument with new score
            			lastDocument = student;
            		}
            		continue;
            	} else {
            		//If other student, delete last document, as it is the lowest score of last student
            		collection.deleteOne(lastDocument);
            		
            		//Update lastDocument with new student
            		lastDocument = student;
            	}
            }
        }
    }
	

}
