package br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto;

import java.util.UUID;

public record CidadeInfoOutputDto(
        UUID id,

        String nome,

        String estado,

        String tempoViagem,

        boolean arquivado) {
}
