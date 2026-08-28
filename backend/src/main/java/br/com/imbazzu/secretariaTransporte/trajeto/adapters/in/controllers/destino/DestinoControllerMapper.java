package br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.destino;

import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.destino.dto.DestinoResponseDto;
import br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto.DestinoInfoOutputDto;

public class DestinoControllerMapper {

    public static DestinoResponseDto paraDestinoResponse(DestinoInfoOutputDto dto){
        return new DestinoResponseDto(dto.idDestino(),dto.nomeDestino(),dto.idCidade());
    }
}
