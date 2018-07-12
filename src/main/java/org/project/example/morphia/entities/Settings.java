package org.project.example.morphia.entities;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Settings {
    public String defaultBranch = "master";
    public Boolean allowWiki = false;
    public Boolean allowIssues = true;
}
