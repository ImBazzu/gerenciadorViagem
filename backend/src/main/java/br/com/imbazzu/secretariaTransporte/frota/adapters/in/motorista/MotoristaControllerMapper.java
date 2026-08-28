package br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista;

import br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista.dto.MotoristaRequestDto;
import br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista.dto.MotoristaResponseDto;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.command.dto.MotoristaCommandSalvarDto;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto;

public class MotoristaControllerMapper {

    public static MotoristaResponseDto paraResponseDto(MotoristaQueryBaseInfoDto dto){
        return new MotoristaResponseDto(dto.id().toString(),
                dto.nome(),dto.cpf(), dto.telefone(), dto.tipoVeiculo());
    }

    public static MotoristaCommandSalvarDto paraCommandSalvarDto(MotoristaRequestDto dto){
        return new MotoristaCommandSalvarDto(
                dto.nome(),dto.cpf(),dto.telefone(),dto.tipoMotorista()
        );
    }
}
