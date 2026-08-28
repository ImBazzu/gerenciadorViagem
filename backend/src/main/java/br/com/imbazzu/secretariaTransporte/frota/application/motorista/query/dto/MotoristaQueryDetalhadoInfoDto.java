package br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto;

import java.util.List;
import java.util.UUID;

public record MotoristaQueryDetalhadoInfoDto(
        UUID id,
        String nome,
        String telefone,
        String cpf,
        List<VeiculoGatewayQueryInfoDto> tipoVeiculo
) {
}
