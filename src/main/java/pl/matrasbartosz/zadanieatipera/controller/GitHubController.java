package pl.matrasbartosz.zadanieatipera.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;
import pl.matrasbartosz.zadanieatipera.entity.GitHubResponse;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;
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

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GitHubResponse handleUsernameNotExistsException(UsernameNotFoundException e) {
        return new GitHubResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public GitHubResponse handleInvalidContentTypeException(HttpMediaTypeNotAcceptableException e) {
        return new GitHubResponse(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
    }
}
