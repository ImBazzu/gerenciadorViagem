package br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.gateways;

import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.VeiculoGatewayQueryInfoDto;

import java.util.List;
import java.util.UUID;

public interface VeiculoGatewayPorta {

    List<VeiculoGatewayQueryInfoDto> buscarPorIdMotorista(UUID id);
}
