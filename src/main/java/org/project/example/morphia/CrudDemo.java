package org.project.example.morphia;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.project.example.morphia.entities.GitHubUser;
import org.project.example.morphia.entities.Organization;
import org.project.example.morphia.entities.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CrudDemo {

    private Date date;
    private Datastore datastore;


    public CrudDemo() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        date = sdf.parse("6-15-1987");
        Morphia morphia = new Morphia();
        datastore = morphia.createDatastore(new MongoClient(), "morphia-demo");
        datastore.getDB().dropDatabase();
        morphia.mapPackage("org.mongodb.morphia.demo");
        datastore.ensureIndexes();
    }

    public void save() {

        GitHubUser evanchooly = new GitHubUser("evanchooly");
        evanchooly.setFullName("Justin Lee");
        evanchooly.setMemberSince(date);
        evanchooly.setFollowing(1000);
        datastore.save(evanchooly);  //Insert

        Organization org = new Organization("mongodb");
        datastore.save(org); // Insert

        Repository morphia1 = new Repository(org, "morphia");
        Repository morphia2 = new Repository(evanchooly, "morphia");

        datastore.save(morphia1); // Insert
        datastore.save(morphia2); // Insert

        evanchooly.getRepositories().add(morphia1);
        evanchooly.getRepositories().add(morphia2);

        datastore.save(evanchooly); // Update
    }

    public void query() {
        Query<Repository> query = datastore.createQuery(Repository.class);

        Repository repository = query.get(); // FindOne

        List<Repository> repositories = query.asList(); // Find and fetch all in memory

        Iterable<Repository> fetch = query.fetch(); // Find and get cursor

        ((MorphiaIterator)fetch).close(); // Close the cursor. But it can be reused

        Iterator<Repository> iterator = fetch.iterator(); // Get and iterator
        while(iterator.hasNext()) {
            Repository r = iterator.next();
        }

        iterator = fetch.iterator(); // Can be reused
        while(iterator.hasNext()) {
            iterator.next();
        }

        // FindOne with filter
        GitHubUser memberSince = datastore.createQuery(GitHubUser.class).field("memberSince").equal(date).get();
        System.out.println("memberSince = " + memberSince);

        // Can use the Java field name or the MongoDB field name
        GitHubUser since = datastore.createQuery(GitHubUser.class).field("since").equal(date).get();
        System.out.println("since = " + since);
    }

    public void updates() {
        GitHubUser evanchooly = datastore.createQuery(GitHubUser.class).field("userName").equal("evanchooly").get();
        evanchooly.setFollowers(12);
        evanchooly.setFollowing(678);
        datastore.save(evanchooly);
    }

    public void batchUpdate() {
        UpdateOperations<GitHubUser> update = datastore.createUpdateOperations(GitHubUser.class)
                .inc("followers")
                .set("following", 42);
        Query<GitHubUser> query = datastore.createQuery(GitHubUser.class).field("followers").equal(0);
        datastore.update(query, update);
    }

    public void versioned() {
        Organization organization = datastore.createQuery(Organization.class).get();
        Organization organization2 = datastore.createQuery(Organization.class).get();

        //Assert.assertEquals(organization.version, 1L);
        datastore.save(organization);

        //Assert.assertEquals(organization.version, 2L);
        datastore.save(organization);

        //Assert.assertEquals(organization.version, 3L); Will throw ConcurrentModificationException
        datastore.save(organization2);
    }
}
