package br.com.imbazzu.frontSpring.service;

import br.com.imbazzu.frontSpring.dto.motorista.MotoristaRequestDto;
import br.com.imbazzu.frontSpring.dto.motorista.MotoristaResponseDto;
import br.com.imbazzu.frontSpring.dto.paciente.PacienteResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class MotoristaService {

        private final WebClient webClient;

        private static final String URL_BASE = "/motorista";

        public MotoristaService(@Qualifier("apiWebClient") WebClient webClient) {
                this.webClient = webClient;
        }

        public List<MotoristaResponseDto> buscarMotoristas(String nome) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder.path(URL_BASE)
                                                .queryParam("nome", nome)
                                                .build())
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("buscar motorista", r.statusCode(),
                                                                                b)))
                                .bodyToMono(new ParameterizedTypeReference<List<MotoristaResponseDto>>() {
                                })
                                .block();
        }

        public PacienteResponseDto buscarPacientePorId(Long id) {
                return webClient.get()
                                .uri(URL_BASE + "/" + id)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("buscar motorista por id",
                                                                                r.statusCode(), b)))
                                .bodyToMono(PacienteResponseDto.class)
                                .block();
        }

        public MotoristaResponseDto criar(MotoristaRequestDto dto) {
                return webClient.post()
                                .uri(URL_BASE)
                                .bodyValue(dto)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("criar motorista", r.statusCode(),
                                                                                b)))
                                .bodyToMono(MotoristaResponseDto.class)
                                .block();
        }

        public MotoristaResponseDto atualizar(Long id, MotoristaRequestDto dto) {
                return webClient.put()
                                .uri(URL_BASE + "/" + id)
                                .bodyValue(dto)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("atualizar motorista",
                                                                                r.statusCode(), b)))
                                .bodyToMono(MotoristaResponseDto.class)
                                .block();
        }

        public void excluir(Long id) {
                webClient.delete()
                                .uri(URL_BASE + "/" + id)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("excluir motorista",
                                                                                r.statusCode(), b)))
                                .toBodilessEntity()
                                .block();
        }

        private RuntimeException mapearErro(String acao, HttpStatusCode status, String body) {
                return new RuntimeException(
                                "Erro ao " + acao +
                                                " | Status: " + status +
                                                " | Body: " + body);
        }
}