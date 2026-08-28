package br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.destino.dto;

import java.util.UUID;

public record DestinoResponseDto(
        UUID id,
        String nome,
        UUID idCidade
) {
}
