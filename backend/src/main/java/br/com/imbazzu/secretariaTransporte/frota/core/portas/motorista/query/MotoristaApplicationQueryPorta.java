package br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.query;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryDetalhadoInfoDto;

import java.util.UUID;

public interface MotoristaApplicationQueryPorta {

    MotoristaQueryDetalhadoInfoDto buscarPorId(UUID id);

    ResultadoPaginado<MotoristaQueryDetalhadoInfoDto> buscarPorNome(String nome, int pagina, int tamanho);

}
