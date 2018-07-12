package org.project.example.morphia.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Version;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.Date;

@Entity("orgs")
public class Organization {

    @Id
    private String name;

    @Indexed(value = IndexDirection.ASC, name = "", unique = false, dropDups = false, background = true,
            sparse = false, expireAfterSeconds = -1)
    private Date created;

    @Version("v")  // To detect concurrent updates
    private long version;

    public Organization() {
    }

    public Organization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
