package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.portas;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.domain.PessoaCondicao;

import java.util.Optional;
import java.util.UUID;

public interface PessoaCondicaoBancoPorta {

    Optional<PessoaCondicao> buscarPorId(UUID pessoaCondicaoId);

    PessoaCondicao salvar(PessoaCondicao pessoaCondicao);

    ResultadoPaginado<PessoaCondicao> buscarPorNome(String nome, int pagina, int tamanho);

    boolean existePorNome(String nome);

    boolean existePorNomeIgnorandoId(String nome,UUID pessoaCondicaoId);
}
