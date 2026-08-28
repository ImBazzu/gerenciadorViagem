// br.com.imbazzu.frontSpring.config.ApiClientConfig
package br.com.imbazzu.frontSpring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ApiClientConfig {

    private final JwtAuthorizationFilter authFilter;
    private final JwtRefreshFilter refreshFilter;

    @Bean
    @Qualifier("apiWebClient")
    public WebClient apiWebClient(@Value("${api.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(authFilter) // adiciona header
                .filter(refreshFilter) // intercepta 401 e tenta refresh
                .build();
    }

    @Configuration
    public static class AuthClientConfig {
        @Bean
        @Qualifier("authWebClient")
        public WebClient authWebClient(@Value("${api.base-url}") String baseUrl) {
            return WebClient.builder()
                    .baseUrl(baseUrl)
                    .build();
        }
    }
}
