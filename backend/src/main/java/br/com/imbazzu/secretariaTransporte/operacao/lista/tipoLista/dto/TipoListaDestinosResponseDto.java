package br.com.imbazzu.secretariaTransporte.operacao.lista.tipoLista.dto;

import java.util.UUID;

public record TipoListaDestinosResponseDto(
        UUID id,
        String nome) {
}
