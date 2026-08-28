package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.portas;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.domain.Pessoa;

import java.util.Optional;
import java.util.UUID;

public interface PessoaBancoPorta {

    Optional<Pessoa> buscarPorId(UUID idPessoa);

    ResultadoPaginado<Pessoa> buscarPorNome(String nome, int pagina, int tamanho);

    Pessoa salvar(Pessoa pessoa);

    boolean verificarExistenciaCpf(String cpf);

    boolean verificarExistenciaCpfIgnorandoId(String cpf, UUID idPessoa);
}
