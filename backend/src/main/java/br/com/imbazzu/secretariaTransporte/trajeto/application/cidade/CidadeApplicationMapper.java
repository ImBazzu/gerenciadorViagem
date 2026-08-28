package br.com.imbazzu.secretariaTransporte.trajeto.application.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.Cidade;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.valueObject.TempoViagem;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;

public class CidadeApplicationMapper {

    public static CidadeInfoOutputDto paraCidadeInfo(Cidade cidade) {

        return new CidadeInfoOutputDto(
                cidade.getId(),
                cidade.getNome().valor(),
                cidade.getEstado().name(),
                cidade.getTempoViagem().tempoFormatado(),
                cidade.isArquivado()
        );
    }

    public static Cidade paraDominio(CidadeSalvarInputDto cidadeNova) {
        return Cidade.criar(new Nome(cidadeNova.nome()),
                cidadeNova.estado(),new TempoViagem(cidadeNova.horas(),cidadeNova.minutos()));
    }



}
