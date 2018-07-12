package org.project.example.morphia.entities;

import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(value = "users", noClassnameStored = true)
@Indexes({
        @Index(value = "userName,-followers", name = "popular"),
        @Index(value = "lastActive", name = "idle", expireAfterSeconds = 1000000000) // purge documents after seconds
})
public class GitHubUser {

    @Id
    private String userName;
    private String fullName;
    @Property("since")
    private Date memberSince;
    private Date lastActive;
    @Reference(lazy = true)
    private List<Repository> repositories = new ArrayList<>();
    private int followers = 0;
    private int following = 0;

    public GitHubUser() {
    }

    public GitHubUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince = memberSince;
    }

    public Date getLastActive() {
        return lastActive;
    }

    public void setLastActive(Date lastActive) {
        this.lastActive = lastActive;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
