package pl.matrasbartosz.zadanieatipera.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.matrasbartosz.zadanieatipera.entity.Branch;
import pl.matrasbartosz.zadanieatipera.entity.Repository;
import pl.matrasbartosz.zadanieatipera.entity.RepositoryResponse;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    private final WebClient webClient;

    private static final Function<String, String> toRepoUrl = ("/users/%s/repos")::formatted;
    private static final BiFunction<String, String, String> toBranchUrl = ("/repos/%s/%s/branches")::formatted;

    public List<RepositoryResponse> getRepositoriesByUserName(String userName) {
        Gson gson = new Gson();
        var repoUrl = toRepoUrl.apply(userName);
        var retrievedRepositories = getRepositoriesForUserName(repoUrl);

        TypeToken<List<Repository>> listTypeToken = new TypeToken<>() {};
        List<Repository> gitHubUsers = gson.fromJson(retrievedRepositories, listTypeToken.getType());

        List<RepositoryResponse> repositoryResponses = gitHubUsers.stream()
                .filter(repository -> !repository.fork())
                .flatMap(repository -> {
                    var retrievedBranches = retrieveBranches(toBranchUrl.apply(repository.owner().login(), repository.name()));
                    TypeToken<List<Branch>> branchToken = new TypeToken<>() {
                    };
                    List<Branch> branches = gson.fromJson(retrievedBranches, branchToken.getType());
                    return Stream.of(new RepositoryResponse(repository, branches));
                })
                .toList();
        logger.info("Repositories for user {} - {}", userName, repositoryResponses);
        return repositoryResponses;
    }

    private String getRepositoriesForUserName(String repoUrl) {
        return webClient.get()
                .uri(repoUrl)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.error(new UsernameNotFoundException("User does not exist")))
                .bodyToMono(String.class)
                .block();
    }


    private String retrieveBranches(String branchesUrl) {
        return webClient.get()
                .uri(branchesUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
