package br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto;

import java.util.UUID;

public record DestinoInfoOutputDto(UUID idCidade, UUID idDestino, String nomeDestino) {
}
