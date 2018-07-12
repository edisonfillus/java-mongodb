package org.project.example.morphia.entities;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("repos")
public class Repository {

    @Id
    private String name;

    @Reference
    private Organization organization;

    @Reference
    private GitHubUser owner;

    private Settings settings = new Settings();

    public Repository() {
    }

    public Repository(final Organization organization, final String name) {
        this.organization = organization;
        this.name = organization.getName() + "/" + name;
    }

    public Repository(final GitHubUser owner, final String name) {
        this.owner = owner;
        this.name = owner.getUserName() + "/" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

}


