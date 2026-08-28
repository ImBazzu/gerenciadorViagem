package br.com.imbazzu.frontSpring.dto.paciente;

import br.com.imbazzu.frontSpring.PessoaTipoEnum;
import br.com.imbazzu.frontSpring.util.DtoUtils;

public record PacienteResponseDto(String id,
        String nome,
                                  String cpf,
                                  String telefone,
                                  String endereco,
                                  PessoaTipoEnum tipo) {

    public PacienteResponseDto{
        telefone = DtoUtils.formatarTelefone(telefone);
        cpf = DtoUtils.formatarCpf(cpf);
    }

}

