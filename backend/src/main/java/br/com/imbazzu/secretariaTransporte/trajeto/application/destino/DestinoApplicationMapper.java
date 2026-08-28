package br.com.imbazzu.secretariaTransporte.trajeto.application.destino;

import br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto.DestinoInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain.Destino;

public class DestinoApplicationMapper {

    public static DestinoInfoOutputDto paraDestinoInfo(Destino destino){
        return new DestinoInfoOutputDto(destino.getIdCidade(),destino.getId(),destino.getNome().valor());
    }
}
