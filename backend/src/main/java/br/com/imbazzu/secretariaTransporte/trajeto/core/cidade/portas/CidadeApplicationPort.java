package br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.portas;

import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;

import java.util.UUID;

public interface CidadeApplicationPort {

    void arquivar(UUID id);

    CidadeInfoOutputDto buscarPorId(UUID id);

    ResultadoPaginado<CidadeInfoOutputDto> buscarPorNome(String nome, int pagina, int tamanho);

    CidadeInfoOutputDto editar(UUID id, CidadeSalvarInputDto dto);

    void desarquivar(UUID id);

    CidadeInfoOutputDto salvar(CidadeSalvarInputDto dto);

}
