package org.project.example.spark;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.bson.Document;
import org.project.example.App;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.halt;

public class HelloWorldMongoDBSparkFreemarkerStyle {
    public static void main(String[] args)  {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(App.class,"/freemarker");

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into TemplateException-s.
        cfg.setWrapUncheckedExceptions(true);

        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));

        MongoDatabase database = client.getDatabase("course");
        final MongoCollection<Document> collection = database.getCollection("hello");
        collection.drop();

        collection.insertOne(new Document("name", "MongoDB"));

        get("/", (req, res) -> {
            //Map<String, Object> helloMap = new HashMap<>();
            //helloMap.put("name", "Freemarker");

            StringWriter writer = new StringWriter();
            try {
                Template helloTemplate = cfg.getTemplate("hello.ftl");
                Document document = collection.find().first();
                //helloTemplate.process(helloMap,writer);
                helloTemplate.process(document, writer);
            } catch (TemplateException e) {
                halt(500);
                e.printStackTrace();
            } catch (IOException e) {
                halt(500);
                e.printStackTrace();
            }
            return writer;
        });
    }
}
