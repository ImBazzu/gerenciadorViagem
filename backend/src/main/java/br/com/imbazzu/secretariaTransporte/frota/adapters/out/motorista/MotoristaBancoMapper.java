package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Cpf;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Telefone;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.MotoristaTipoEnum;

public class MotoristaBancoMapper {

    public static MotoristaBanco paraMotoristaBanco(Motorista motorista) {
        return new MotoristaBanco(
                motorista.getId(),
                motorista.getNome().valor(),
                motorista.getCpf().valor(),
                motorista.getTelefone().valor(),
                motorista.getTipoMotorista().toString(),
                motorista.isArquivado());
    }

    public static Motorista paraMotorista(MotoristaBanco motorista) {
        return Motorista.recriar(motorista.getId(),
                new Nome(motorista.getNome()),
                new Cpf(motorista.getCpf()),
                new Telefone(motorista.getTelefone()),
                MotoristaTipoEnum.valueOf(motorista.getTipoMotorista()),
                motorista.isArquivado());
    }
}
