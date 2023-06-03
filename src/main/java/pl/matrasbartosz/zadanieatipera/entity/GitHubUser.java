package pl.matrasbartosz.zadanieatipera.entity;

import java.util.List;

public class GitHubUser {

    private String repositoryName;
    private String ownerLogin;
    private List<GitHubBranch> branches;

    public GitHubUser(String repositoryName, String ownerLogin, List<GitHubBranch> branches) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerLogin;
        this.branches = branches;

    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public List<GitHubBranch> getBranches() {
        return branches;
    }

    @Override
    public String toString() {
        return "GitHubUser{" +
                "repositoryName='" + repositoryName + '\'' +
                ", ownerLogin='" + ownerLogin + '\'' +
                ", branches=" + branches +
                '}';
    }
}
