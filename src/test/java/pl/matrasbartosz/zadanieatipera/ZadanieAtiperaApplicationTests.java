package pl.matrasbartosz.zadanieatipera;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.matrasbartosz.zadanieatipera.controller.GitHubController;
import pl.matrasbartosz.zadanieatipera.service.GitHubService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
class ZadanieAtiperaApplicationTests {

    @Autowired
    private GitHubController gitHubController;
    @Autowired
    private GitHubService gitHubService;

    @Test
    void contextLoads() {
        assertThat(gitHubController, notNullValue());

        assertThat(gitHubService, notNullValue());
    }

}
