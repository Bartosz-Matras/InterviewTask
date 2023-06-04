package pl.matrasbartosz.zadanieatipera.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubConfiguration {

    private static final String GITHUB_API_URL = "https://api.github.com";

    @Bean
    @ConditionalOnProperty(prefix = "github.access", name = "token")
    @Value("${github.access.token}")
    public WebClient webClientOAuth(String accessToken) {
        return WebClient.builder()
                .baseUrl(GITHUB_API_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "github.access", name = "token", havingValue = "false", matchIfMissing = true)
    public WebClient webClientWithoutOAuth() {
        return WebClient.builder()
                .baseUrl(GITHUB_API_URL)
                .build();
    }

}
