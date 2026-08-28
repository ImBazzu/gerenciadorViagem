package br.com.imbazzu.frontSpring.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.imbazzu.frontSpring.dto.viagem.ViagemPorPeriodoResponseDto;
import br.com.imbazzu.frontSpring.dto.viagem.ViagemResponseDto;
import br.com.imbazzu.frontSpring.dto.viagem.ViagemRequestDto;

@Service
public class ViagemService {

        private final WebClient webClient;
        private final String URL_VIAGEM = "/viagem";

        public ViagemService(@Qualifier("apiWebClient") WebClient webClient) {
                this.webClient = webClient;
        }

        public ViagemResponseDto buscarDtoPorId(Long id) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder.path(URL_VIAGEM + "/" + id).build())
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b ->
                                                                // Log the error body for debugging
                                                                mapearErro("buscar viagem por ID", r.statusCode(),
                                                                                b)))
                                .bodyToMono(ViagemResponseDto.class)
                                .block();
        }

        public ViagemPorPeriodoResponseDto buscarViagem(LocalDate data) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder.path(URL_VIAGEM)
                                                .queryParam("data", data)
                                                .build())
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b ->
                                                                // Log the error body for debugging
                                                                mapearErro("obter todas as viagens", r.statusCode(),
                                                                                b)))
                                .bodyToMono(ViagemPorPeriodoResponseDto.class)
                                .block();
        }

        public ViagemResponseDto moverPassageiro(Long idViagem, Long idPassageiro) {
                return webClient.put()
                                .uri(uriBuilder -> uriBuilder.path(URL_VIAGEM + "/" + idViagem + "/" + idPassageiro)
                                                .build())
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b ->
                                                                // Log the error body for debugging
                                                                mapearErro("mover passageiro", r.statusCode(),
                                                                                b)))
                                .bodyToMono(ViagemResponseDto.class)
                                .block();
        }

        public void salvar(ViagemRequestDto dto) {
                webClient.post()
                                .uri(uriBuilder -> uriBuilder.path(URL_VIAGEM).build())
                                .bodyValue(dto)
                                .retrieve()
                                .onStatus(
                                                HttpStatusCode::isError,
                                                r -> r.bodyToMono(String.class)
                                                                .map(b ->
                                                                // Log the error body for debugging
                                                                mapearErro("salvar viagem", r.statusCode(),
                                                                                b)))
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
