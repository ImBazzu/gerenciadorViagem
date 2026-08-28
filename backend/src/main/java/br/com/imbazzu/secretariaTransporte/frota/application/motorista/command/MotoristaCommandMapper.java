package br.com.imbazzu.secretariaTransporte.frota.application.motorista.command;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Cpf;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Telefone;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.command.dto.MotoristaCommandSalvarDto;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;

public class MotoristaCommandMapper {

    public static Motorista paraDomain(MotoristaCommandSalvarDto dto){
        return Motorista.criar(new Nome(dto.nome()),new Cpf(dto.cpf()),new Telefone(dto.telefone()),dto.idVeiculos()
                );
    }

    public static void editarMotorista(Motorista motorista, MotoristaCommandSalvarDto dto){
        motorista.editar(new Nome(dto.nome()),new Cpf(dto.cpf()),new Telefone(dto.telefone()),
                dto.idVeiculos());
    }
}
