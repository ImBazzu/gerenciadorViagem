package br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.portas;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaSalvarInputDto;

import java.util.UUID;

public interface PessoaApplicationPorta {

    PessoaInfoOutputDto buscarPorId(UUID id);

    PessoaInfoOutputDto salvar(PessoaSalvarInputDto dto);

    PessoaInfoOutputDto editar(UUID  id, PessoaSalvarInputDto dto);

    void arquivar(UUID idPessoa);

    void desarquivar(UUID idPessoa);

    ResultadoPaginado<PessoaInfoOutputDto> listarPorNome(String nome, int pagina, int tamanho);
}
