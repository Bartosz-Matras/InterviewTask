package pl.matrasbartosz.zadanieatipera.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;

import java.util.ArrayList;
import java.util.Collections;
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

    public GitHubUser getRepositoriesByUserName(String userName) {
        List<String> repositories = new ArrayList<>();
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
                    JSONObject jsonObject = array.getJSONObject(i);
                    if (!jsonObject.getBoolean("fork")) {
                        repositories.add(jsonObject.getString("name"));
                    }
                }
            }

            logger.info("Retrieving repositories {} for user {} from url {}{}", repositories, userName, GITHUB_API_URL, repoUrl);
            return new GitHubUser(userName, repositories, true);
        } catch (WebClientResponseException.NotFound ex) {
            logger.info("User not found {} - {}", userName, GITHUB_API_URL + repoUrl);
            return new GitHubUser(userName, Collections.emptyList(), false);
        }
    }

}
