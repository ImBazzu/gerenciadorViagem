package br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DestinoSalvarInputDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotNull(message = "Obrigatório informar o id da Cidade")
        UUID destino_id) {
}
