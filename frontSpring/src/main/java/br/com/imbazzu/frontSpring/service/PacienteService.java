package br.com.imbazzu.frontSpring.service;

import br.com.imbazzu.frontSpring.dto.ErroApiDto;
import br.com.imbazzu.frontSpring.dto.PaginacaoDtoRecord;
import br.com.imbazzu.frontSpring.dto.paciente.PacienteRequestDto;
import br.com.imbazzu.frontSpring.dto.paciente.PacienteResponseDto;
import br.com.imbazzu.frontSpring.exception.ApiException;
import br.com.imbazzu.frontSpring.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class PacienteService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private static final String URL_BASE = "/paciente";

    public PacienteService(@Qualifier("apiWebClient") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public PaginacaoDtoRecord<PacienteResponseDto> buscarPacientes(String nome, String tipo, int pagina) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URL_BASE)
                        .queryParam("nome", nome)
                        .queryParam("tipo", tipo)
                        .queryParam("page", pagina)
                        .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        r -> r.bodyToMono(String.class)
                                .map(b -> mapearErro("buscar pacientes", r.statusCode(), b))
                )
                .bodyToMono(new ParameterizedTypeReference<PaginacaoDtoRecord<PacienteResponseDto>>() {})
                .block();
    }

    public PacienteResponseDto buscarPacientePorId(String id) {
        return webClient.get()
                .uri(URL_BASE + "/" + id)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        r -> r.bodyToMono(String.class)
                                .map(b -> mapearErro("buscar paciente por id", r.statusCode(), b))
                )
                .bodyToMono(PacienteResponseDto.class)
                .block();
    }

    public void criar(PacienteRequestDto dto) {
        webClient.post()
                .uri(URL_BASE)
                .bodyValue(dto)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        r -> r.bodyToMono(String.class)
                                .map(b -> mapearErro("criar passageiro", r.statusCode(), b))
                )
                .bodyToMono(PacienteResponseDto.class)
                .block();
    }

    public PacienteResponseDto atualizar(String id, PacienteRequestDto dto) {
        return webClient.put()
                .uri(URL_BASE + "/" + id)
                .bodyValue(dto)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        r -> r.bodyToMono(String.class)
                                .map(b -> mapearErro("atualizar paciente", r.statusCode(), b))
                )
                .bodyToMono(PacienteResponseDto.class)
                .block();
    }

    public void excluir(String id) {
        webClient.delete()
                .uri(URL_BASE + "/" + id)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        r -> r.bodyToMono(String.class)
                                .map(b -> mapearErro("excluir paciente", r.statusCode(), b))
                )
                .toBodilessEntity()
                .block();
    }
    private RuntimeException mapearErro(
            String acao,
            HttpStatusCode status,
            String body) {

        if (status.value() == 409 || status.value() == 400) {
            return new RegraNegocioException(extrairMensagem(body));
        }

        return new ApiException(
                "Erro ao " + acao +
                        " | Status: " + status +
                        " | Body: " + body
        );
    }

    private String extrairMensagem(String body) {
        try {
            ErroApiDto erro = objectMapper.readValue(body, ErroApiDto.class);
            return erro.mensagem();
        } catch (Exception e) {
            // fallback: se o body não vier no formato esperado, evita quebrar o fluxo
            return "Ocorreu um erro ao processar a solicitação.";
        }
    }
}
