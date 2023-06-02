package pl.matrasbartosz.zadanieatipera.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matrasbartosz.zadanieatipera.entity.GitHubResponse;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;
import pl.matrasbartosz.zadanieatipera.exceptions.InvalidContentTypeException;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;
import pl.matrasbartosz.zadanieatipera.service.GitHubService;

@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GitHubController {

    private final Logger logger = LoggerFactory.getLogger(GitHubController.class);

    private final GitHubService gitHubService;

    @GetMapping(value = "/repositories/{userName}")
    public ResponseEntity<GitHubUser> getUserRepositories(@PathVariable String userName,
                                                @RequestHeader(HttpHeaders.CONTENT_TYPE) String header) {
        if (!header.equals(MediaType.APPLICATION_JSON_VALUE)) {
            throw new InvalidContentTypeException("Invalid content type");
        }
        logger.info("Get all repositories for user: {}", userName);
        GitHubUser gitHubUser = this.gitHubService.getRepositoriesByUserName(userName);
        if (!gitHubUser.isUserExist()) {
            throw new UsernameNotFoundException("User not exist");
        }
        return ResponseEntity.ok(gitHubUser);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<GitHubResponse> handleUsernameNotExistsException(UsernameNotFoundException e) {
        logger.info("User not found");
        GitHubResponse response = new GitHubResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<GitHubResponse> handleInvalidContentTypeException(InvalidContentTypeException e) {
        logger.info("Invalid content type");
        GitHubResponse response = new GitHubResponse(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }
}
