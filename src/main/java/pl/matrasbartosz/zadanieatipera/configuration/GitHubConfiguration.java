package pl.matrasbartosz.zadanieatipera.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubConfiguration {

    private static final String GITHUB_API_URL = "https://api.github.com";

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(GITHUB_API_URL)
                .build();
    }
}
