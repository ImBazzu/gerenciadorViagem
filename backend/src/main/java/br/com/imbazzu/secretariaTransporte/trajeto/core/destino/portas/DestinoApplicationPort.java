package br.com.imbazzu.secretariaTransporte.trajeto.core.destino.portas;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto.DestinoInfoOutputDto;

import java.util.UUID;

public interface DestinoApplicationPort {


    void arquivar(UUID idDestino);

    DestinoInfoOutputDto buscarPorId(UUID idDestino);

    ResultadoPaginado<DestinoInfoOutputDto>buscarPorNome
            (String destinoNome, int numPagina, int tamanhoPagina);

    ResultadoPaginado<DestinoInfoOutputDto>buscarPorNomeECidade
            (UUID idCidade, String destinoNome, int numPagina, int tamanhoPagina);

    DestinoInfoOutputDto editar(UUID idDestino, String nomeDestino);

    void desarquivar(UUID idDestino);

    DestinoInfoOutputDto salvar(UUID idCidade, String nomeDestino);

}
