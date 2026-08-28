package br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Dados para criar um novo conjunto de cidades.
 */
public record ConjuntoRequestDto(
        @NotBlank(message = "Nome do conjunto é obrigatório")
        String nome,

        @NotNull(message = "Informe ao menos uma cidade")
        Set<UUID> idsCidades
) {}
