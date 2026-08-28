package br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Cpf;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Telefone;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.domain.Pessoa;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.valueObjects.Endereco;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.valueObjects.Observacao;

public class PessoaApplicationMapper {

    public static PessoaInfoOutputDto paraPessoaInfo(Pessoa pessoa) {
        return new  PessoaInfoOutputDto(pessoa.getId(), pessoa.getNome().valor(),
                pessoa.getCpf().valor(), pessoa.getPessoaCondicaoId(),
                pessoa.getTelefone().valor(),pessoa.getEndereco().valor(),
                pessoa.getObservacao().valor());
    }

}
