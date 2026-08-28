package br.com.imbazzu.frontSpring.service;

import br.com.imbazzu.frontSpring.dto.destino.DestinoResponseDto;
import br.com.imbazzu.frontSpring.exception.FormularioError;
import br.com.imbazzu.frontSpring.dto.destino.DestinoRequestDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DestinoService {

        private final WebClient webClient;

        private static final String URL_BASE = "/destino";

        public DestinoService(@Qualifier("apiWebClient") WebClient webClient) {
                this.webClient = webClient;
        }

        public List<DestinoResponseDto> buscarDestinos(String nome) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder.path(URL_BASE)
                                                .queryParam("nome", nome)
                                                .build())
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("buscar Destino", r.statusCode(),
                                                                                b)))
                                .bodyToMono(new ParameterizedTypeReference<List<DestinoResponseDto>>() {
                                })
                                .block();
        }

        public DestinoResponseDto criar(DestinoRequestDto dto) {
                DestinoResponseDto retorno;
                try {
                        retorno = webClient.post()
                                        .uri(URL_BASE)
                                        .bodyValue(dto)
                                        .retrieve()
                                        .onStatus(
                                                        HttpStatusCode::isError,
                                                        r -> r.bodyToMono(String.class)
                                                                        .map(b -> mapearErro("criar destino",
                                                                                        r.statusCode(),
                                                                                        b)))
                                        .bodyToMono(DestinoResponseDto.class)
                                        .block();

                } catch (RuntimeException e) {
                        throw new RuntimeException("Erro ao criar destino: " + e.getMessage(), e);
                }

                return retorno;
        }

        public DestinoResponseDto atualizar(Long id, DestinoRequestDto dto) {
                return webClient.put()
                                .uri(URL_BASE + "/" + id)
                                .bodyValue(dto)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("atualizar destino",
                                                                                r.statusCode(), b)))
                                .bodyToMono(DestinoResponseDto.class)
                                .block();
        }

        public void excluir(Long id) {
                webClient.delete()
                                .uri(URL_BASE + "/" + id)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("excluir destino",
                                                                                r.statusCode(), b)))
                                .toBodilessEntity()
                                .block();
        }

        public DestinoResponseDto buscarPorId(Long id) {
                return webClient.get()
                                .uri(URL_BASE + "/" + id)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b -> mapearErro("buscar destino por id",
                                                                                r.statusCode(), b)))
                                .bodyToMono(DestinoResponseDto.class)
                                .block();
        }

        private Throwable mapearErro(String operacao, HttpStatusCode status, String body) {
                String mensagem = body;

                if (status.value() == 409 || status.value() == 404 || status.value() == 400) {
                        throw new FormularioError(mensagem);
                }

                return new RuntimeException("Falha ao " + operacao + ": " + mensagem);
        }
}