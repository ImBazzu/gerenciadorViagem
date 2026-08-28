package br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto;

import java.util.UUID;

public record MotoristaQueryBaseInfoDto(
        UUID id,
        String nome,
        String telefone,
        String cpf
) {
}
