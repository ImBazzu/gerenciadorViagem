package br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.dto;

import java.util.List;
import java.util.UUID;

public record ConjuntoResponseDto(UUID id, String nome, List<String> cidades) {
}
