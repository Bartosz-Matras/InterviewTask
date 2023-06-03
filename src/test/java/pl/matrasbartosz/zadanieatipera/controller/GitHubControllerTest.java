package pl.matrasbartosz.zadanieatipera.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubControllerTest {

    @LocalServerPort
    int randomServerPort;

}