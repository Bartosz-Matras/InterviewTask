package pl.matrasbartosz.zadanieatipera.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;
import pl.matrasbartosz.zadanieatipera.service.GitHubService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GitHubController {

    private final Logger logger = LoggerFactory.getLogger(GitHubController.class);

    private final GitHubService gitHubService;

    @GetMapping(value = "/repositories/{userName}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<GitHubUser> getUserRepositories(@PathVariable String userName) {
        logger.info("Request to get all repositories for user: {}", userName);
        return this.gitHubService.getRepositoriesByUserName(userName);
    }

}
