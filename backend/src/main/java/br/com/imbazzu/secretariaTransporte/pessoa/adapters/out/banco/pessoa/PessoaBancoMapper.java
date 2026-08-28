package br.com.imbazzu.secretariaTransporte.pessoa.adapters.out.banco.pessoa;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Cpf;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Telefone;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.domain.Pessoa;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.valueObjects.Endereco;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.valueObjects.Observacao;

public class PessoaBancoMapper {

    public static PessoaBanco paraEntityJpa(Pessoa pessoa) {
        return new PessoaBanco(pessoa.getId(),pessoa.getNome().valor(),
                pessoa.getCpf().valor(),pessoa.getPessoaCondicaoId(),
                pessoa.getTelefone().valor(),pessoa.getEndereco().valor(),
                pessoa.getObservacao().valor(),pessoa.isArquivado());
    }

    public static Pessoa paraDomainPessoa(PessoaBanco pessoa) {
        return Pessoa.recriar(pessoa.getId(),new Nome(pessoa.getNome()),
                new Cpf(pessoa.getCpf()),new Telefone(pessoa.getTelefone()),pessoa.getPessoaCondicaoId(),
                new Endereco(pessoa.getEndereco()),new Observacao(pessoa.getObservacao()),pessoa.isArquivador());
    }
}
