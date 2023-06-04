package pl.matrasbartosz.zadanieatipera.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.matrasbartosz.zadanieatipera.entity.GitHubBranch;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GitHubService {

    public static final String IS_FORK = "fork";
    public static final String REPOSITORY_NAME = "name";
    public static final String REPOSITORY_OWNER = "owner";
    public static final String REPOSITORY_LOGIN = "login";
    public static final String BRANCH_NAME = "name";
    public static final String BRANCH_COMMIT = "commit";
    public static final String LAST_COMMIT_SHA = "sha";
    private final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    @Value("${github.access.token}")
    private String accessToken;

    private final WebClient webClient;

    private static final Function<String, String> toRepoUrl = ("/users/%s/repos")::formatted;
    private static final BiFunction<String, String, String> toBranchUrl = ("/repos/%s/%s/branches")::formatted;
    
    public List<GitHubUser> getRepositoriesByUserName(String userName) {
        List<GitHubUser> repositories = new ArrayList<>();
        String repoUrl = toRepoUrl.apply(userName);

        try {
            String retrievedRepositories = getRepositoriesForUserName(repoUrl);
            JSONArray array = new JSONArray(retrievedRepositories);
            for (int i = 0; i < array.length(); i++) {
                JSONObject repository = array.getJSONObject(i);
                if (!repository.getBoolean(IS_FORK)) {
                    String repositoryName = repository.getString(REPOSITORY_NAME);
                    JSONObject owner = repository.getJSONObject(REPOSITORY_OWNER);
                    String ownerName = owner.getString(REPOSITORY_LOGIN);

                    List<GitHubBranch> branches =
                            getBranchesForRepository(toBranchUrl.apply(ownerName, repositoryName));
                    repositories.add(new GitHubUser(
                            repositoryName,
                            ownerName,
                            branches
                    ));
                }
            }
            logger.info("Retrieving repositories {} for user {} from url {}", repositories, userName, repoUrl);
            return repositories;
        } catch (WebClientResponseException.NotFound ex) {
            logger.info("User not found {} - {}", userName, repoUrl);
            throw new UsernameNotFoundException("User not exist");
        }
    }

    public String getRepositoriesForUserName(String repoUrl) {
        return webClient.get()
                .uri(repoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public List<GitHubBranch> getBranchesForRepository(String branchesURL) {
        List<GitHubBranch> branches = new ArrayList<>();
        String retrievedBranches = retrieveBranches(branchesURL);

        JSONArray array = new JSONArray(retrievedBranches);
        for (int i = 0; i < array.length(); i++) {
            JSONObject branch = array.getJSONObject(i);
            String branchName = branch.getString(BRANCH_NAME);
            JSONObject commit = branch.getJSONObject(BRANCH_COMMIT);
            String commitSha = commit.getString(LAST_COMMIT_SHA);
            branches.add(new GitHubBranch(branchName, commitSha));
        }

        return branches;
    }

    private String retrieveBranches(String branchesUrl) {
        return webClient.get()
                .uri(branchesUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
