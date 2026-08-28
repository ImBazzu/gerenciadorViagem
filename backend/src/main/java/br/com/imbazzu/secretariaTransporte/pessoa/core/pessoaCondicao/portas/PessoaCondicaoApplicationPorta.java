package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.portas;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoSalvarInputDto;

import java.util.UUID;

public interface PessoaCondicaoApplicationPorta {

    PessoaCondicaoInfoOutputDto salvar(PessoaCondicaoSalvarInputDto dto);

    PessoaCondicaoInfoOutputDto buscarPorId(UUID id);

    PessoaCondicaoInfoOutputDto editar(UUID id, PessoaCondicaoSalvarInputDto dto);

    void arquivar(UUID id);

    void desarquivar(UUID id);

    ResultadoPaginado<PessoaCondicaoInfoOutputDto> buscarPorNome(String nome, int pagina, int tamanho);
}
