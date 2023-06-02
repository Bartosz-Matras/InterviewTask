package pl.matrasbartosz.zadanieatipera.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class GitHubUser {

    private String userName;
    private List<String> repositories;
    @JsonIgnore
    private boolean userExist;

    public GitHubUser(String userName, List<String> repositories, boolean userExist) {
        this.userName = userName;
        this.repositories = repositories;
        this.userExist = userExist;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public boolean isUserExist() {
        return userExist;
    }

    public void setUserExist(boolean userExist) {
        this.userExist = userExist;
    }
}
