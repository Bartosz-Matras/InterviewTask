package pl.matrasbartosz.zadanieatipera.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.matrasbartosz.zadanieatipera.entity.GitHubBranch;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {

    private final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    private static final String GITHUB_API_URL = "https://api.github.com";

    private final WebClient webClient;

    public GitHubService() {
        this.webClient = WebClient.builder()
                .baseUrl(GITHUB_API_URL)
                .build();
    }

    public List<GitHubUser> getRepositoriesByUserName(String userName) {
        List<GitHubUser> repositories = new ArrayList<>();
        String repoUrl = "/users/" + userName + "/repos";

        try {
            List<String> retrievedRepositories = webClient.get()
                    .uri(repoUrl)
                    .retrieve()
                    .bodyToFlux(String.class)
                    .collectList()
                    .block();

            if(retrievedRepositories != null) {
                JSONArray array = new JSONArray(retrievedRepositories.get(0));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject repository = array.getJSONObject(i);
                    if (!repository.getBoolean("fork")) {
                        String repositoryName = repository.getString("name");
                        JSONObject owner = repository.getJSONObject("owner");
                        String ownerName = owner.getString("login");
                        String branchUrl = GITHUB_API_URL +
                                "/repos/" +
                                ownerName +
                                "/" +
                                repositoryName +
                                "/branches";

                        List<GitHubBranch> branches = getBranchesForRepository(branchUrl);
                        repositories.add(new GitHubUser(
                                repositoryName,
                                ownerName,
                                branches
                        ));
                    }
                }
            }

            logger.info("Retrieving repositories {} for user {} from url {}{}", repositories, userName, GITHUB_API_URL, repoUrl);
            return repositories;
        } catch (WebClientResponseException.NotFound ex) {
            logger.info("User not found {} - {}", userName, GITHUB_API_URL + repoUrl);
            throw new UsernameNotFoundException("User not exist");
        }
    }

    public List<GitHubBranch> getBranchesForRepository(String branchesURL) {
        List<GitHubBranch> branches = new ArrayList<>();
        List<String> retrievedBranches = webClient.get()
                .uri(branchesURL)
                .retrieve()
                .bodyToFlux(String.class)
                .collectList()
                .block();
        if(retrievedBranches != null) {
            JSONArray array = new JSONArray(retrievedBranches.get(0));
            for (int i = 0; i < array.length(); i++) {
                JSONObject branch = array.getJSONObject(i);
                String branchName = branch.getString("name");
                JSONObject commit = branch.getJSONObject("commit");
                String commitSha = commit.getString("sha");
                branches.add(new GitHubBranch(branchName, commitSha));
            }
        }

        return branches;
    }

}
