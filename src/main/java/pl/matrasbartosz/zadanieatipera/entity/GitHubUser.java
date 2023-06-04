package pl.matrasbartosz.zadanieatipera.entity;

import java.util.List;

public record GitHubUser(String repositoryName, String ownerLogin, List<GitHubBranch> branches) {
}
