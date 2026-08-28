package br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Dados para adicionar ou remover uma cidade de um conjunto existente.
 */
public record ConjuntosRequestDto(
        @NotNull(message = "Id da cidade é obrigatório")
        UUID idCidade
) {}
