// br.com.imbazzu.frontSpring.config.AuthService
package br.com.imbazzu.frontSpring.config;

import br.com.imbazzu.frontSpring.dto.login.LoginRequestDto;
import br.com.imbazzu.frontSpring.dto.login.LoginResponseDto;
import br.com.imbazzu.frontSpring.dto.login.RefreshRequestDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthService {


    private final WebClient webClient;

    public AuthService(@Qualifier("authWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        return webClient.post()
                .uri("/auth/login")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(LoginResponseDto.class)
                .block();
    }

    public LoginResponseDto refresh(String refreshToken) {
        return webClient.post()
                .uri("/auth/atualizarToken")
                .bodyValue(new RefreshRequestDto(refreshToken))
                .retrieve()
                .bodyToMono(LoginResponseDto.class)
                .block();
    }
}
