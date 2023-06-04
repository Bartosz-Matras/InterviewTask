package pl.matrasbartosz.zadanieatipera.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import pl.matrasbartosz.zadanieatipera.entity.GitHubUser;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    void controllerShouldReturnNotFoundStatusIfUserNotExists() {
        //given
        String url = "http://localhost:" + randomServerPort + "/api/v1/github/repositories/Bartosz-Matrasss";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        //when
        ResponseEntity<String> forEntity =
                this.testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        //then
        assertThat(forEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(forEntity.getBody(), equalTo("{\"status\":404,\"message\":\"User not exist\"}"));
    }

    @Test
    void controllerShouldReturnNotAcceptableStatusForInvalidContentType() {
        //given
        String url = "http://localhost:" + randomServerPort + "/api/v1/github/repositories/Bartosz-Matras";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/xml");
        //when
        ResponseEntity<String> forEntity =
                this.testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        //then
        assertThat(forEntity.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE));
        assertThat(forEntity.getBody(), equalTo("{\"status\":406,\"message\":\"Content-Type 'application/xml' is not supported\"}"));
    }

    @Test
    void controllerShouldReturnOkStatus() {
        //given
        String url = "http://localhost:" + randomServerPort + "/api/v1/github/repositories/Bartosz-Matras";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        //when
        ResponseEntity<List<GitHubUser>> forEntity =
                this.testRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {});
        //then
        assertThat(forEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(forEntity.getBody()).size(), equalTo(16));
        assertThat(forEntity.getBody().get(0).repositoryName(), equalTo("AdventureDemo"));
        assertThat(forEntity.getBody().get(0).ownerLogin(), equalTo("Bartosz-Matras"));
        assertThat(forEntity.getBody().get(0).branches().get(0).branchName(), equalTo("main"));
        assertThat(forEntity.getBody().get(0).branches().get(0).lastCommitSha(), equalTo("fbb30458d30cf0af054ba0fec6a6edbdd76d8adf"));

    }

}
