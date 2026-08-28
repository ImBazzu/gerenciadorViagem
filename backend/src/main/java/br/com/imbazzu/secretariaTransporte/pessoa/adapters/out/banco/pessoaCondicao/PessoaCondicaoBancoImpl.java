package br.com.imbazzu.secretariaTransporte.pessoa.adapters.out.banco.pessoaCondicao;

import br.com.imbazzu.secretariaTransporte.compartilhados.adapter.outbound.persistence.mapper.PageMapper;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.domain.PessoaCondicao;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.portas.PessoaCondicaoBancoPorta;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PessoaCondicaoBancoImpl implements PessoaCondicaoBancoPorta {

    private final PessoaCondicaoRepository banco;

    public PessoaCondicaoBancoImpl(PessoaCondicaoRepository banco) {
        this.banco = banco;
    }

    @Override
    public Optional<PessoaCondicao> buscarPorId(UUID pessoaCondicaoId) {
        return banco.findById(pessoaCondicaoId).map(
                p->{
                    return PessoaCondicao.recriar(p.getId(),new Nome(p.getNome()),p.isAtivo());}
        );
    }

    @Override
    public PessoaCondicao salvar(PessoaCondicao pessoaCondicao) {
        var pessoaCondicaoBanco = new PessoaCondicaoBanco(pessoaCondicao);
        var pessoaSalva = banco.save(pessoaCondicaoBanco);
        return PessoaCondicao.recriar(pessoaSalva.getId(),new Nome(pessoaSalva.getNome()),pessoaSalva.isAtivo());
    }

    @Override
    public ResultadoPaginado<PessoaCondicao> buscarPorNome(String nome, int pagina, int tamanho) {
        var pageable = PageRequest.of(pagina, tamanho);
        var paginado = banco.buscarPorNome(nome,pageable).map(p->{
            return PessoaCondicao.recriar(p.getId(),new Nome(p.getNome()),p.isAtivo());
        });
        return PageMapper.toDomain(paginado);
    }

    @Override
    public boolean existePorNome(String nome) {
        return banco.existsByNome(nome);
    }

    @Override
    public boolean existePorNomeIgnorandoId(String nome, UUID pessoaCondicaoId) {
        return banco.existsByNomeAndIdIgnoreCase(nome,pessoaCondicaoId);
    }
}
