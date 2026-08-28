package br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade.dto;

import java.util.UUID;

public record CidadeResponseDto(UUID id, String nome, String estado, String tempoViagem) {
}
