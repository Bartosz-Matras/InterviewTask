package pl.matrasbartosz.zadanieatipera.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.matrasbartosz.zadanieatipera.entity.*;
import pl.matrasbartosz.zadanieatipera.exceptions.UsernameNotFoundException;
import pl.matrasbartosz.zadanieatipera.service.GitHubService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitHubController.class)
class GitHubMockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    private List<RepositoryResponse> prepareGitHubUserData() {
        RepositoryResponse repositoryResponse1 = new RepositoryResponse(
                new Repository("angular-hardware-store", false, new Owner("Bartosz-Matras")),
                List.of(new Branch("main", new Commit("7232982a536c47a973e59d6540eba4a1df38f256")))
        );

        RepositoryResponse repositoryResponse2 = new RepositoryResponse(
                new Repository("BookShop", false, new Owner("Bartosz-Matras")),
                List.of(new Branch("main", new Commit("6f71c4f046c9756e49a51f97fbe7396c5fa9babb")))
        );

        return Arrays.asList(repositoryResponse1, repositoryResponse2);
    }

    @Test
    void controllerShouldReturnNotFoundStatusIfUserNotExists() throws Exception {
        when(gitHubService.getRepositoriesByUserName("Bartosz-Matrasss")).thenThrow(new UsernameNotFoundException("User not exist"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/github/repositories/Bartosz-Matrasss")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("{\"status\":404,\"message\":\"User not exist\"}")));
    }

    @Test
    void controllerShouldReturnNotAcceptableStatusForInvalidContentType() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/github/repositories/Bartosz-Matras")
                .contentType(MediaType.APPLICATION_XML_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotAcceptable())
                .andExpect(content().string(containsString("{\"status\":406,\"message\":\"Content-Type 'application/xml' is not supported\"}")));
    }

    @Test
    void controllerShouldReturnOkStatus() throws Exception {
        List<RepositoryResponse> users = prepareGitHubUserData();

        when(gitHubService.getRepositoriesByUserName("Bartosz-Matrasss")).thenReturn(users);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/github/repositories/Bartosz-Matrasss")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(users.get(0).repository().owner().login())))
                .andExpect(content().string(containsString(users.get(0).repository().name())));
    }

}
